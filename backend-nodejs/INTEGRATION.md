# Node.js integration onto dev

Branch: `feat/clinic-posts-from-dev`.

## Sources

- Base: `origin/dev` at `610a1ae3befb9484e3bdc6bf1c5706492473948f`.
- Node.js and Postman: `feat/admin-clinic-posts` at `ac27684234adcaa322fddb74e18d3ef9cbe64440`.
- Additional Docker seed helper: `feat/clinic-posts` at `32d41d64dafd5556486207dff827365f81d7f00a`.
- Both source branches remain intact. This branch transfers selected paths rather than merging their unrelated Android/Spring Boot history.

## Decisions

- Keep the admin branch's automatic approval, legacy approval/rejection, status counts, pagination, clinic avatar responses, and automatic migrations 001–004.
- Keep port 3000, matching the Android base URL on dev; do not restore the older clinic branch's port 3001 settings.
- Keep the admin branch's latest statistics rule: only ACTIVE clinics count as active. Update the stale Postman descriptions that counted PENDING_PROFILE as active.
- Use `compose.nodejs.yaml` for Node.js only. Do not import the older combined Spring Boot Compose setup or its backend changes.
- CI tests the new feature branch and PRs into dev; EC2 deployment is restricted to dev.

## Verification

- 35 Node.js tests pass, including HTTP routes, authorization, automatic approval, legacy moderation, pagination, and migration recovery.
- Migration tests use a simulated database; a live MySQL migration and end-to-end login with Spring Boot have not been run.
- Postman JSON and PowerShell seed helper syntax validate. Docker Compose configuration validates without building or starting containers.
- `frontend/`, `backend/`, and `.github/workflows/CI-BE.yml` match the pinned dev base.

## Existing Android contract differences to resolve separately

- `ApiAdminHome.clinicPostAdmin` declares `ApiResponse<ClinicPostResponseNodeJs>`, while `GET /api/v1/admin/clinic-posts` returns an array in `data`. The client should deserialize a list.
- `ClinicPostResponseNodeJs` declares `approveAt` and `createAt`; Node.js returns `approvedAt` and `createdAt`. The client needs matching property names or serialization annotations.
- Android files remain as supplied by dev; the passing Node.js tests do not establish end-to-end Android compatibility.
