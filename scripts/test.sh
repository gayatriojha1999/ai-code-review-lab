#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p build/classes
find src/main/java src/test/java -name '*.java' -print0 | xargs -0 javac --release 17 -d build/classes
java -cp build/classes dev.reviewlab.CodeReviewLabTest
