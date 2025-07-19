# Makefile for MintScan CLI
# Simplifies build, test, and run operations

# Variables
MAVEN := mvn
JAVA := java
DOCKER := docker
DOCKER_COMPOSE := docker-compose
JAR_NAME := mint_scan-cli.jar
JAR_PATH := target/$(JAR_NAME)
DOCKER_IMAGE := mint_scan-cli:latest
DOCKER_BUILD_IMAGE := maven:3.9.5-eclipse-temurin-11

# Colors for output
RED := \033[0;31m
GREEN := \033[0;32m
YELLOW := \033[1;33m
BLUE := \033[0;34m
NC := \033[0m # No Color

# Default target
.DEFAULT_GOAL := help

# Detect if Java/Maven are available
HAS_JAVA := $(shell command -v java 2> /dev/null)
HAS_MAVEN := $(shell command -v mvn 2> /dev/null)
HAS_DOCKER := $(shell command -v docker 2> /dev/null)

# Phony targets
.PHONY: help build build-jar build-docker build-all clean run test install \
        docker-build docker-run docker-clean login list retrieve process \
        check-java check-maven check-docker dev-setup \
        native-build docker-native-build

# Help target
help: ## Show this help message
	@echo "$(BLUE)MintScan CLI - Makefile Commands$(NC)"
	@echo ""
	@echo "$(YELLOW)Build Commands:$(NC)"
	@grep -E '^(build|clean|install|test).*:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)Docker Commands:$(NC)"
	@grep -E '^docker-.*:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)CLI Commands:$(NC)"
	@grep -E '^(login|list|retrieve|process).*:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)Other Commands:$(NC)"
	@grep -E '^(run|dev-setup|check).*:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)Examples:$(NC)"
	@echo "  make build          # Build using available tools"
	@echo "  make docker-build   # Build using Docker"
	@echo "  make login USER=usuario PASS=password"
	@echo "  make list TOKEN=your-token-here"
	@echo ""

# Build targets
build: ## Build the project (auto-detects Java/Docker)
ifdef HAS_MAVEN
	@echo "$(GREEN)Building with local Maven...$(NC)"
	@$(MAKE) build-jar
else ifdef HAS_DOCKER
	@echo "$(YELLOW)Maven not found. Building with Docker...$(NC)"
	@$(MAKE) docker-build
else
	@echo "$(RED)Error: Neither Maven nor Docker found!$(NC)"
	@echo "Install either Maven+Java or Docker to build the project."
	@exit 1
endif

build-jar: check-maven ## Build JAR with Maven
	@echo "$(BLUE)Building JAR file...$(NC)"
	$(MAVEN) clean package
	@echo "$(GREEN)✓ Build successful! JAR created at $(JAR_PATH)$(NC)"

build-docker: check-docker ## Build Docker image
	@echo "$(BLUE)Building Docker image...$(NC)"
	$(DOCKER) build -t $(DOCKER_IMAGE) .
	@echo "$(GREEN)✓ Docker image built: $(DOCKER_IMAGE)$(NC)"

build-all: ## Build both JAR and Docker image
ifdef HAS_MAVEN
	@$(MAKE) build-jar
endif
	@$(MAKE) build-docker

# Clean targets
clean: ## Clean build artifacts
	@echo "$(BLUE)Cleaning build artifacts...$(NC)"
ifdef HAS_MAVEN
	$(MAVEN) clean
else ifdef HAS_DOCKER
	$(DOCKER) run --rm -v $(PWD):/workspace -w /workspace $(DOCKER_BUILD_IMAGE) mvn clean
endif
	@echo "$(GREEN)✓ Clean complete$(NC)"

# Docker-specific targets
docker-build: check-docker ## Build using Docker (no local Java needed)
	@echo "$(BLUE)Building with Docker...$(NC)"
	@./docker-build.sh jar
	@echo "$(GREEN)✓ Docker build complete$(NC)"

docker-run: check-docker ## Run CLI using Docker
	@./docker-run.sh $(ARGS)

docker-clean: check-docker ## Clean Docker images and containers
	@echo "$(BLUE)Cleaning Docker artifacts...$(NC)"
	-$(DOCKER) rmi $(DOCKER_IMAGE) 2>/dev/null
	-$(DOCKER) volume rm java_maven-cache 2>/dev/null
	@echo "$(GREEN)✓ Docker clean complete$(NC)"

# Installation target
install: build ## Install JAR to local system
	@echo "$(BLUE)Installing MintScan CLI...$(NC)"
	@mkdir -p ~/bin
	@cp $(JAR_PATH) ~/bin/
	@echo '#!/bin/bash' > ~/bin/mint_scan-cli
	@echo 'java -jar ~/bin/$(JAR_NAME) "$$@"' >> ~/bin/mint_scan-cli
	@chmod +x ~/bin/mint_scan-cli
	@echo "$(GREEN)✓ Installed to ~/bin/mint_scan-cli$(NC)"
	@echo "$(YELLOW)Add ~/bin to your PATH if not already added:$(NC)"
	@echo '  export PATH=$$PATH:~/bin'

# Test target
test: check-maven ## Run tests
	@echo "$(BLUE)Running tests...$(NC)"
	$(MAVEN) test
	@echo "$(GREEN)✓ Tests complete$(NC)"

# Native image targets
native-build: ## Build native executable with GraalVM
	@echo "$(BLUE)Building native image with GraalVM...$(NC)"
	@echo "$(YELLOW)Note: This requires GraalVM with native-image installed$(NC)"
ifdef HAS_MAVEN
	$(MAVEN) clean package -Pnative
else
	@echo "$(RED)Error: Maven is required for native build$(NC)"
	@exit 1
endif
	@echo "$(GREEN)✓ Native image built: target/mintscan-cli$(NC)"

docker-native-build: check-docker ## Build native image using Docker (GraalVM)
	@echo "$(BLUE)Building native image with Docker (GraalVM)...$(NC)"
	@echo "$(GREEN)=== MintScan Native Image Docker Build ===$(NC)"
	@echo ""
	@echo "$(YELLOW)Step 1: Building JAR with Maven...$(NC)"
	$(DOCKER) run --rm -v $(PWD):/workspace -w /workspace \
		maven:3.9.5-eclipse-temurin-11 \
		mvn clean package
	@echo ""
	@echo "$(YELLOW)Step 2: Building native executable with GraalVM...$(NC)"
	$(DOCKER) run --rm -v $(PWD):/workspace -w /workspace \
		ghcr.io/graalvm/graalvm-ce:ol9-java11-22.3.3 \
		bash -c "gu install native-image && \
			native-image -jar target/mint_scan-cli.jar \
			--no-fallback \
			--enable-url-protocols=http,https \
			-H:Name=target/mintscan-cli \
			-H:ReflectionConfigurationFiles=src/main/resources/META-INF/native-image/reflect-config.json \
			-H:ResourceConfigurationFiles=src/main/resources/META-INF/native-image/resource-config.json"
	@echo ""
	@echo "$(GREEN)✓ Native build complete!$(NC)"
	@echo "$(YELLOW)Native executable created at: $(BLUE)target/mintscan-cli$(NC)"
	@echo ""
	@echo "$(YELLOW)To run the native executable:$(NC)"
	@echo "  ./target/mintscan-cli --help"

# Run target
run: ## Run the CLI (shows help)
ifdef HAS_JAVA
	@$(JAVA) -jar $(JAR_PATH) $(ARGS)
else ifdef HAS_DOCKER
	@$(MAKE) docker-run ARGS="$(ARGS)"
else
	@echo "$(RED)Error: Neither Java nor Docker found!$(NC)"
	@exit 1
endif

# CLI command shortcuts
login: ## Login to MintScan API (use USER and PASS variables)
ifndef USER
	@echo "$(RED)Error: USER variable required$(NC)"
	@echo "Usage: make login USER=usuario [PASS=password]"
	@exit 1
endif
	@$(MAKE) run ARGS="login $(USER) $(PASS)"

list: ## List documents (use TOKEN variable or MINTSCAN_TOKEN env)
ifndef TOKEN
ifndef MINTSCAN_TOKEN
	@echo "$(RED)Error: TOKEN variable or MINTSCAN_TOKEN env required$(NC)"
	@echo "Usage: make list TOKEN=your-token"
	@echo "   or: export MINTSCAN_TOKEN=your-token"
	@exit 1
endif
endif
ifdef TOKEN
	@$(MAKE) run ARGS="list --token $(TOKEN) $(ARGS)"
else
	@$(MAKE) run ARGS="list $(ARGS)"
endif

retrieve: ## Retrieve document (use ID and TOKEN variables)
ifndef ID
	@echo "$(RED)Error: ID variable required$(NC)"
	@echo "Usage: make retrieve ID=document-id TOKEN=your-token"
	@exit 1
endif
ifndef TOKEN
ifndef MINTSCAN_TOKEN
	@echo "$(RED)Error: TOKEN variable or MINTSCAN_TOKEN env required$(NC)"
	@exit 1
endif
endif
ifdef TOKEN
	@$(MAKE) run ARGS="retrieve $(ID) --token $(TOKEN) $(ARGS)"
else
	@$(MAKE) run ARGS="retrieve $(ID) $(ARGS)"
endif

process: ## Process image (use FILE, TYPE, CATEGORY variables)
ifndef FILE
	@echo "$(RED)Error: FILE variable required$(NC)"
	@echo "Usage: make process FILE=doc.pdf TYPE=coc CATEGORY=M1"
	@exit 1
endif
ifndef TYPE
	@echo "$(RED)Error: TYPE variable required$(NC)"
	@echo "Types: coc, titv-old, titv-new, reduced, single-approval, cdc"
	@exit 1
endif
ifndef CATEGORY
	@echo "$(RED)Error: CATEGORY variable required$(NC)"
	@echo "Categories: M1, M3, N1, N3, L, O, T, TR, OS, OSR"
	@exit 1
endif
ifndef TOKEN
ifndef MINTSCAN_TOKEN
	@echo "$(RED)Error: TOKEN variable or MINTSCAN_TOKEN env required$(NC)"
	@exit 1
endif
endif
ifdef TOKEN
	@$(MAKE) run ARGS="process --token $(TOKEN) --tipo $(TYPE) --categoria $(CATEGORY) $(FILE) $(ARGS)"
else
	@$(MAKE) run ARGS="process --tipo $(TYPE) --categoria $(CATEGORY) $(FILE) $(ARGS)"
endif

# Development setup
dev-setup: ## Setup development environment
	@echo "$(BLUE)Setting up development environment...$(NC)"
ifdef HAS_DOCKER
	@echo "$(GREEN)✓ Docker found$(NC)"
else
	@echo "$(YELLOW)⚠ Docker not found - install from https://docs.docker.com/get-docker/$(NC)"
endif
ifdef HAS_JAVA
	@echo "$(GREEN)✓ Java found: $(shell java -version 2>&1 | head -n 1)$(NC)"
else
	@echo "$(YELLOW)⚠ Java not found - install Java 11 or higher$(NC)"
endif
ifdef HAS_MAVEN
	@echo "$(GREEN)✓ Maven found: $(shell mvn -version | head -n 1)$(NC)"
else
	@echo "$(YELLOW)⚠ Maven not found - install Maven 3.6 or higher$(NC)"
endif
	@echo ""
	@echo "$(BLUE)Creating directories...$(NC)"
	@mkdir -p data output
	@echo "$(GREEN)✓ Setup complete$(NC)"

# Check prerequisites
check-java:
ifndef HAS_JAVA
	@echo "$(RED)Error: Java not found!$(NC)"
	@echo "Please install Java 11 or higher"
	@exit 1
endif

check-maven:
ifndef HAS_MAVEN
	@echo "$(RED)Error: Maven not found!$(NC)"
	@echo "Please install Maven 3.6 or higher"
	@exit 1
endif

check-docker:
ifndef HAS_DOCKER
	@echo "$(RED)Error: Docker not found!$(NC)"
	@echo "Please install Docker from https://docs.docker.com/get-docker/"
	@exit 1
endif

# Advanced targets for CI/CD
.PHONY: ci package release

ci: clean test build ## Run CI pipeline
	@echo "$(GREEN)✓ CI pipeline complete$(NC)"

package: build ## Create distribution package
	@echo "$(BLUE)Creating distribution package...$(NC)"
	@mkdir -p dist
	@cp $(JAR_PATH) dist/
	@cp mint_scan-cli dist/
	@cp README.md dist/
	@cp CLI_README.md dist/
	@tar -czf mint_scan-cli-dist.tar.gz -C dist .
	@rm -rf dist
	@echo "$(GREEN)✓ Package created: mint_scan-cli-dist.tar.gz$(NC)"

release: clean build test package ## Create release
	@echo "$(GREEN)✓ Release ready$(NC)"

# Docker compose shortcuts
.PHONY: compose-up compose-down compose-build

compose-up: ## Start services with docker-compose
	$(DOCKER_COMPOSE) up -d

compose-down: ## Stop services with docker-compose
	$(DOCKER_COMPOSE) down

compose-build: ## Build with docker-compose
	$(DOCKER_COMPOSE) --profile build run builder