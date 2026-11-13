# Ticket 03 — Unauthorized read in a hypothetical document-sharing API

**Not yet implemented — extension exercise.**

Build a minimal `DocumentService` that has `getDocument(userId, documentId)` and a permission store. Implement a deliberately flawed candidate that looks up a document but skips the ownership/membership permission check.

**Acceptance criteria:** A document must only be returned to its owner or an explicitly authorized member. Unknown users and unauthorized users must receive the same safe denial behaviour, without exposing document contents. Add regression tests and a written review distinguishing authentication from authorization.

Do not claim this exercise is complete until you implement and test it.
