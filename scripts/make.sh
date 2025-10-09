#!/bin/bash
set -euo pipefail

java -version
./mvnw -f ay clean install -Pnative
mkdir -p bin
cp ay/target/ay bin/entrypoint

ls -liah bin/entrypoint

echo "Native build complete."