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

# Print image size and summary info
docker images ay:latest

# Login and push to public ECR
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
AWS_REGION=${AWS_REGION:-"us-east-1"}
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Check if repository exists, if not create it. 
aws ecr describe-repositories --repository-names ay --region $AWS_REGION \
    || aws ecr create-repository --repository-name ay --region $AWS_REGION


docker tag ay:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/ay:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/ay:latest

# Print image details URL on public ECR
GALLERY_URL="$(aws ecr describe-repositories --repository-names ay \
    --region $AWS_REGION --query "repositories[0].repositoryUri" \
    --output text\
        | sed 's|\.dkr\.ecr\..*||')/ay:latest"
echo "Image pushed to ECR Public: $GALLERY_URL"

# Print docker run command to run the image
echo "To run the image, use the following command:"
echo "docker run $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/ay:latest"

popd
echo "Native build complete."
