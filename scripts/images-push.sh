#!/bin/bash
set -euo pipefail
DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd "$DIR/.."

export AWS_PAGER=""

# --- Build image
docker build \
  --no-cache \
  --progress=plain \
  -f Containerfile \
  -t ay:latest \
  .

# Print image size and summary info
docker images ay:latest

# --- Login to Amazon ECR Public (auth region is us-east-1)
PUB_REGION="us-east-1"
aws ecr-public get-login-password --region "$PUB_REGION" \
  | docker login --username AWS --password-stdin public.ecr.aws

# --- Ensure repository exists (public ECR)
aws ecr-public describe-repositories --region "$PUB_REGION" --repository-names ay >/dev/null 2>&1 \
  || aws ecr-public create-repository --region "$PUB_REGION" --repository-name ay >/dev/null

# --- Resolve your public registry URI (e.g., public.ecr.aws/xxxx)
REGISTRY_URI="$(aws ecr-public describe-registries --region "$PUB_REGION" --query 'registries[0].registryUri' --output text)"
# Extract the alias/namespace part after "public.ecr.aws/"
REGISTRY_ALIAS="${REGISTRY_URI#public.ecr.aws/}"

# --- Tag & push
PUBLIC_IMAGE_URI="${REGISTRY_URI}/ay:latest"
docker tag ay:latest "$PUBLIC_IMAGE_URI"
docker push "$PUBLIC_IMAGE_URI"

# --- Public gallery URL (shareable web page)
IMAGE_URL="https://gallery.ecr.aws/${REGISTRY_ALIAS}/ay"
echo "Image pushed to ECR Public: $PUBLIC_IMAGE_URI"
echo "Public gallery page: $IMAGE_URL"

# --- How to run (pulls from public)
echo "To run the image:"
echo "docker run public.ecr.aws/${REGISTRY_ALIAS}/ay:latest"

popd
echo "Public ECR build & push complete."
