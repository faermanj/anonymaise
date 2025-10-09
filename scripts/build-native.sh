#!/bin/bash
set -euo pipefail
DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd $DIR/..

./mvnw -f ay clean package -Pnative 

popd
echo "Native build complete."
