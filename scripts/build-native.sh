#!/bin/bash
set -euo pipefail
# get script dir
DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd $DIR/..

pushd "ay"
export MAVEN_OPTS="--sun-misc-unsafe-memory-access=allow"
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
popd

# The native binary will be in target/ (e.g., target/ay-*-runner)
echo "Native build complete. Check the target/ directory for the binary."
popd
