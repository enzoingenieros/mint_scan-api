#!/bin/bash
#
# Docker run wrapper for MintITV CLI
# Runs the CLI using Docker without requiring Java on host
#

set -e

# Script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Check if Docker is available
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker is not installed${NC}"
    echo "Please install Docker or use the JAR directly with Java"
    exit 1
fi

# Check if image exists, build if not
if ! docker image inspect mintitv-cli:latest &> /dev/null; then
    echo -e "${GREEN}Docker image not found. Building...${NC}"
    ./docker-build.sh image
fi

# Build docker run command with proper environment variable handling
DOCKER_CMD="docker run --rm -i"

# Add TTY if available (for interactive password input)
if [ -t 0 ]; then
    DOCKER_CMD="$DOCKER_CMD -t"
fi

# Pass environment variables safely
if [ -n "$MINTITV_USER" ]; then
    DOCKER_CMD="$DOCKER_CMD -e MINTITV_USER"
fi

if [ -n "$MINTITV_PASS" ]; then
    DOCKER_CMD="$DOCKER_CMD -e MINTITV_PASS"
fi

if [ -n "$MINTITV_TOKEN" ]; then
    DOCKER_CMD="$DOCKER_CMD -e MINTITV_TOKEN"
fi

# Mount current directory for file access
DOCKER_CMD="$DOCKER_CMD -v $(pwd):/data -w /data"

# Add image name
DOCKER_CMD="$DOCKER_CMD mintitv-cli:latest"

# Run the CLI
exec $DOCKER_CMD "$@"