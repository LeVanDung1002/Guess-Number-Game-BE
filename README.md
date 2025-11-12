# 🎯 Number Guessing Game - Microservices Architecture

A distributed number guessing game built with Spring Boot Microservices, Redis, Kafka, and PostgreSQL.

## 🏗️ Architecture Overview

## 📦 Microservices

| Service | Port | Description | Technologies |
|---------|------|-------------|--------------|
| 🎪 **Eureka Server** | 8761 | Service Discovery & Registry | Spring Cloud Eureka |
| 🚪 **API Gateway** | 8080 | Single Entry Point, Routing, Load Balancing | Spring Cloud Gateway |
| 👤 **User Service** | 8081 | Authentication & User Management | Spring Boot, JPA, PostgreSQL |
| 🎮 **Game Service** | 8082 | Game Logic & Number Guessing | Spring Boot, Kafka, PostgreSQL |
| 🏆 **Leaderboard Service** | 8083 | Real-time Rankings & Caching | Spring Boot, Redis |

## 🛠️ Technology Stack

### Backend
- **Java 17** + **Spring Boot 3.1.0**
- **Spring Cloud** - Microservices ecosystem
- **Spring Security** + **JWT** - Authentication
- **Spring Data JPA** - Database operations
- **Spring Data Redis** - Caching
- **Spring Kafka** - Event streaming

### Database & Messaging
- **PostgreSQL** - Primary database
- **Redis** - Caching & Leaderboard
- **Apache Kafka** - Event streaming
- **Zookeeper** - Kafka coordination

### Infrastructure
- **Docker** - Containerization
- **Docker Compose** - Multi-container management
- **Eureka** - Service discovery
- **Maven** - Dependency management

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+ 
- Docker & Docker Compose
- Git

# Start all infrastructure services in background
docker-compose up -d

# Verify containers are running
docker ps

## 🚀 Start Microservices

cd eureka-server
mvn spring-boot:run

cd api-gateway
mvn spring-boot:run

cd user-service
mvn spring-boot:run

cd game-service
mvn spring-boot:run


cd leaderboard-service
mvn spring-boot:run