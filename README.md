# AI Code Review Lab — Java Software Engineering

A **public portfolio practice project** for assessing code correctness, reproducing defects, checking regressions, and writing evidence-based pull-request reviews.

> This repository is an **original practice scaffold created with AI assistance**. It is **not affiliated with Handshake AI, Project Hertz, or any AI lab**. The included candidate implementations are **deliberately flawed teaching examples**, not recorded outputs of a real coding agent. Do not describe them as independent professional experience or as genuine AI-model failure reports.

## Why this project exists

Some software-engineering evaluation roles ask reviewers to take a ticket, evaluate a proposed patch like a teammate's PR, execute tests, identify failure modes, and explain a correct resolution. This repo lets a developer **practice that complete workflow** using realistic Java services.

## Technologies

- Java 17 (standard library; no runtime dependencies)
- Git + pull request workflow
- Concurrency, idempotency, money arithmetic, edge-case testing
- GitHub Actions CI

## Quick start

Requires JDK 17 or later and Bash:

```bash
bash scripts/test.sh
```

The test harness uses only `javac` and `java`; no Maven download is needed.

## Repository layout

```text
src/main/java/dev/reviewlab/
  Inventory.java
  Receipt.java
  FlawedCheckoutService.java
  CorrectCheckoutService.java
  FlawedPricing.java
  CorrectPricing.java
src/test/java/dev/reviewlab/
  CodeReviewLabTest.java
scenarios/
  01-concurrent-idempotency/TICKET.md
  02-money-overflow/TICKET.md
  03-authorization-check/TICKET.md
reviews/
  REVIEW_TEMPLATE.md
  STARTER_EXAMPLE.md
.github/workflows/
  ci.yml
scripts/
  test.sh
```

## Practice protocol (do this yourself)

1. Read one scenario's ticket and acceptance criteria without opening the corrected code.
2. Inspect the candidate implementation and predict failure cases.
3. Run the tests. Add at least **one new test** of your own and explain why it is necessary.
4. Write a PR-style review citing exact file names and lines, reproduction steps, impact, and a suggested remedy.
5. Implement a fix on a new branch and document trade-offs.
6. Compare the corrected behaviour to the acceptance criteria; add a benchmark only if actually measured.
7. Open a GitHub pull request so the code diff, test results, and review rationale are visible.

## Reviewer rubric (practice framework, not an employer rubric)

| Dimension | Weight |
| --- | ---: |
| Correctness and edge-case reasoning | 30% |
| Evidence from reproducible tests | 25% |
| Security, reliability, and concurrency awareness | 20% |
| Clarity of review / actionable feedback | 15% |
| Maintainability and implementation trade-offs | 10% |

## Real-world limitations

- In-memory synchronization demonstrates a **single-process** guarantee. Production idempotency may require transactional database constraints, durable idempotency keys and multi-instance coordination.
- No production workload, organization, or client system is represented.
- Test cases are teaching examples, not evidence of job history.

## What to show in a portfolio

- A clear issue/ticket and acceptance criteria
- An original test that fails before a fix and passes afterward
- PR review comments on specific changes
- An explanation of why the bug mattered
- Honest metadata about what you built, tested, reused, or generated with assistance

## License

MIT; see LICENSE.
