#!/bin/bash
set -euo pipefail

./mvnw -f ay clean install -Pnative
