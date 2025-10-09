#!/bin/bash
set -euo pipefail
DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
pushd $DIR/..

export AWS_PAGER=""

docker build \
    --no-cache \
    --progress=plain \
    -f Containerfile \
    -t ay:latest \
    . 

# Login and push to public ECR
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
AWS_REGION=${AWS_REGION:-"us-east-1"}
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# check if repository exists, if not create it. use variables to avoid long lines
aws ecr describe-repositories --repository-names ay --region $AWS_REGION \
    || aws ecr create-repository --repository-name ay --region $AWS_REGION


docker tag ay:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/ay:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/ay:latest

# Print image details URL on public ECR
GALLERY_URL="https://gallery.ecr.aws/$(echo $ACCOUNT_ID | cut -c1-12)/ay"
echo "Image pushed to ECR Public: $GALLERY_URL"

popd
echo "Native build complete."
