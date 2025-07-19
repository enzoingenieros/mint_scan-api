# Multi-stage build for MintScan CLI

# Stage 1: Build stage
FROM maven:3.9.5-eclipse-temurin-11 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies (cached if pom.xml hasn't changed)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:11-jre-alpine

# Install bash for the wrapper script
RUN apk add --no-cache bash

# Create app user
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/mint_scan-cli.jar .

# Copy wrapper script if needed
COPY mint_scan-cli ./

# Make wrapper executable
RUN chmod +x mint_scan-cli

# Change ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Default command
ENTRYPOINT ["java", "-jar", "/app/mint_scan-cli.jar"]
CMD ["help"]