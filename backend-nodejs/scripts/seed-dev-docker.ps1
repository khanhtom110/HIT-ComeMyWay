param(
  [string]$ContainerName = 'hit-comemyway-nodejs-1',
  [string]$Image = 'comemyway-nodejs:dev'
)

$ErrorActionPreference = 'Stop'
$nodeRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$dockerCommand = Get-Command docker -ErrorAction SilentlyContinue
$dockerCli = if ($dockerCommand) { $dockerCommand.Source } else {
  Join-Path $env:LOCALAPPDATA 'Programs\DockerDesktop\resources\bin\docker.exe'
}
if (-not (Test-Path -LiteralPath $dockerCli)) { throw 'Docker CLI is not available' }

& node (Join-Path $PSScriptRoot 'seed-test-data.js') --prepare
if ($LASTEXITCODE -ne 0) { throw 'Cannot prepare seed credentials' }

$temporaryEnv = Join-Path ([System.IO.Path]::GetTempPath()) (
  'cmw-seed-env-' + [guid]::NewGuid().ToString('N') + '.txt'
)
try {
  $containerEnv = & $dockerCli inspect --format '{{json .Config.Env}}' $ContainerName |
    ConvertFrom-Json
  if ($LASTEXITCODE -ne 0 -or -not $containerEnv) {
    throw 'Cannot read development container environment'
  }
  $allowedKeys = @('DB_HOST', 'DB_PORT', 'DB_NAME', 'DB_USERNAME', 'DB_PASSWORD',
    'JWT_SECRET', 'NODE_ENV')
  $seedEnv = @()
  foreach ($entry in $containerEnv) {
    $separator = $entry.IndexOf('=')
    if ($separator -le 0) { continue }
    $key = $entry.Substring(0, $separator)
    if ($key -in $allowedKeys) { $seedEnv += $entry }
  }
  foreach ($key in @('DB_HOST', 'DB_NAME', 'DB_USERNAME', 'DB_PASSWORD', 'JWT_SECRET')) {
    if (-not ($seedEnv | Where-Object { $_.StartsWith("$key=") })) {
      throw "Missing container setting: $key"
    }
  }
  [System.IO.File]::WriteAllLines($temporaryEnv, [string[]]$seedEnv)
  & $dockerCli run --rm --network "container:$ContainerName" --env-file $temporaryEnv `
    --mount "type=bind,src=$nodeRoot,dst=/seed,readonly" $Image `
    node /seed/scripts/seed-test-data.js
  if ($LASTEXITCODE -ne 0) { throw 'Seed command failed' }
} finally {
  if (Test-Path -LiteralPath $temporaryEnv) {
    Remove-Item -LiteralPath $temporaryEnv -Force
  }
}
