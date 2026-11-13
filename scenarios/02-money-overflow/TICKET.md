# Ticket 02 — Integer overflow in money calculation

**Context:** A batch invoice computes exact totals in cents from quantity and unit price.

**Acceptance criteria**
- Valid nonnegative inputs must produce exact totals without silent overflow.
- Negative inputs must be rejected.
- Extreme inputs must fail explicitly if they exceed the chosen numeric representation.

**Candidate to review:** `FlawedPricing.java` (deliberately flawed, simulated).

**Reproduce:** `bash scripts/test.sh`. The test demonstrates that 50,000 × 50,000 cents does not fit into a Java `int`.

**Review questions:** How does this affect invoices or accounting? Why use integer cents instead of floating-point currency? When would `BigDecimal` be preferable? What input validations are needed?

**Your task:** Write your own code review and tests, then justify the corrected representation.
