#!/bin/bash
set -euo pipefail
# get script dir
DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd $DIR/..

export MAVEN_OPTS="--sun-misc-unsafe-memory-access=allow"
./mvnw -f ay clean package -Pnative -Dquarkus.native.container-build=true

# The native binary will be in target/ (e.g., target/ay-*-runner)
echo "Native build complete. Check the target/ directory for the binary."
popd
