# Ticket 01 — Checkout idempotency under concurrent retries

**Context:** A checkout API accepts a unique `requestId`. Network retries may produce identical requests at nearly the same time.

**Acceptance criteria**
- The same `requestId` with the same SKU and quantity must reserve inventory **exactly once**.
- Both callers must receive the same logical receipt.
- Reusing a key for a different payload must be rejected.
- Insufficient stock must not create a receipt.

**Candidate to review:** `FlawedCheckoutService.java` (deliberately flawed, simulated).

**Reproduce:** `bash scripts/test.sh`. The harness deliberately synchronizes two requests after the non-atomic check and demonstrates the double reservation.

**Review questions:** What is the race? Why doesn't a `ConcurrentHashMap` make this operation atomic? Can locking on one JVM instance guarantee safety across multiple application replicas? What database constraint or transactional boundary would be needed in production?

**Your task:** Write a review note in a new file based on `reviews/REVIEW_TEMPLATE.md`, add an original test, and compare your independently designed fix to the demonstration solution.
