#!/bin/bash
#
# Docker build script for MintScan CLI
# Builds the project using Docker without requiring Java/Maven on host
#

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo -e "${GREEN}=== MintScan CLI Docker Build ===${NC}"
echo

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker is not installed${NC}"
    echo "Please install Docker from https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker daemon is running
if ! docker info &> /dev/null; then
    echo -e "${RED}Error: Docker daemon is not running${NC}"
    echo "Please start Docker and try again"
    exit 1
fi

# Build option
BUILD_TYPE="${1:-jar}"

case "$BUILD_TYPE" in
    "jar"|"build")
        echo -e "${YELLOW}Building JAR using Docker...${NC}"
        
        # Create a temporary container to build the project
        docker run --rm \
            -v "$SCRIPT_DIR":/workspace \
            -w /workspace \
            maven:3.9.5-eclipse-temurin-11 \
            mvn clean package
        
        echo -e "${GREEN}✓ Build completed successfully!${NC}"
        echo
        echo "JAR files created:"
        ls -la target/*.jar
        echo
        echo -e "${GREEN}You can now run the CLI with:${NC}"
        echo "  java -jar target/mintscan-cli.jar"
        echo "  or"
        echo "  ./docker-run.sh <command>"
        ;;
        
    "image")
        echo -e "${YELLOW}Building Docker image...${NC}"
        
        # Build the Docker image
        docker build -t mintscan-cli:latest .
        
        echo -e "${GREEN}✓ Docker image built successfully!${NC}"
        echo
        echo -e "${GREEN}You can now run the CLI with:${NC}"
        echo "  docker run --rm mintscan-cli:latest <command>"
        ;;
        
    "all")
        echo -e "${YELLOW}Building JAR and Docker image...${NC}"
        
        # First build JAR
        docker run --rm \
            -v "$SCRIPT_DIR":/workspace \
            -w /workspace \
            maven:3.9.5-eclipse-temurin-11 \
            mvn clean package
        
        # Then build image
        docker build -t mintscan-cli:latest .
        
        echo -e "${GREEN}✓ Build completed successfully!${NC}"
        echo
        echo "JAR files created:"
        ls -la target/*.jar
        echo
        echo -e "${GREEN}You can run the CLI with:${NC}"
        echo "  java -jar target/mintscan-cli.jar"
        echo "  ./docker-run.sh <command>"
        echo "  docker run --rm mintscan-cli:latest <command>"
        ;;
        
    "clean")
        echo -e "${YELLOW}Cleaning build artifacts...${NC}"
        
        # Clean using Docker
        docker run --rm \
            -v "$SCRIPT_DIR":/workspace \
            -w /workspace \
            maven:3.9.5-eclipse-temurin-11 \
            mvn clean
        
        # Remove Docker image if exists
        if docker image inspect mintscan-cli:latest &> /dev/null; then
            docker rmi mintscan-cli:latest
            echo "Docker image removed"
        fi
        
        echo -e "${GREEN}✓ Clean completed${NC}"
        ;;
        
    *)
        echo -e "${RED}Usage: $0 [jar|image|all|clean]${NC}"
        echo
        echo "Options:"
        echo "  jar    - Build only the JAR file (default)"
        echo "  image  - Build only the Docker image"
        echo "  all    - Build both JAR and Docker image"
        echo "  clean  - Clean build artifacts and Docker image"
        echo
        echo "Examples:"
        echo "  $0          # Build JAR (default)"
        echo "  $0 jar      # Build JAR"
        echo "  $0 image    # Build Docker image"
        echo "  $0 all      # Build everything"
        echo "  $0 clean    # Clean everything"
        exit 1
        ;;
esac