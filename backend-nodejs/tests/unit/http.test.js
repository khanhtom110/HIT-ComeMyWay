import assert from 'node:assert/strict';
import { Readable } from 'node:stream';
import { test } from 'node:test';
import { readJson } from '../../src/http/json.js';

test('JSON preserves Vietnamese text split across UTF-8 byte boundaries', async () => {
  const input = { title: 'Phòng khám', content: 'Tiêm phòng thú cưng' };
  const bytes = Buffer.from(JSON.stringify(input));
  const chunks = Array.from(bytes, byte => Buffer.from([byte]));
  assert.deepEqual(await readJson(Readable.from(chunks)), input);
});

test('JSON rejects requests larger than the byte limit', async () => {
  const stream = Readable.from([Buffer.alloc(64 * 1024 + 1)]);
  await assert.rejects(readJson(stream), { statusCode: 413 });
});
