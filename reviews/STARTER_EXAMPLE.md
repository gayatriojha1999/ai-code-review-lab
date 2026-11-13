# Starter example — concurrency failure review

> **Demonstration only.** This document was included in an AI-assisted starter project and is **not** a personal review or evidence of real AI-agent output. Create your own independently written review for your portfolio.

**Verdict:** Request changes to `FlawedCheckoutService.java`.

**Finding (High severity):** The candidate checks for an existing receipt and then reserves inventory in separate operations. Two concurrent calls with the same idempotency key can both pass the initial check. A thread-safe map does not make the check/reserve/insert transaction atomic.

**Reproduction:** `bash scripts/test.sh` includes a controlled interleaving. With initial stock 5, two identical retries each reserve one unit, leaving stock 3 rather than 4.

**Fix outline:** In this demonstration, `CorrectCheckoutService` serializes operations per instance and rejects mismatched repeat payloads. For a distributed production system, use a durable idempotency record with a uniqueness constraint and make inventory reservation transactional; a JVM monitor is insufficient across replicas.

**Regression criteria:** Concurrent duplicate requests return a matching receipt and decrement inventory once; changed payloads with the same key are rejected.
