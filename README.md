
## 📦 Services

| Service | Port | Description |
|---------|------|-------------|
| 🎪 Eureka Server | 8761 | Service Discovery |
| ⚙️ Config Server | 8888 | Centralized Configuration |
| 🚪 API Gateway | 8080 | API Gateway & Security |
| 👤 User Service | 8081 | Authentication & Users |
| 🎮 Game Service | 8082 | Game Logic |
| 🏆 Leaderboard Service | 8083 | Rankings with Redis |

## 🚀 Quick Start

### Prerequisites
- Java 17
- Maven 3.6+
- Docker & Docker Compose

### 1. Start Infrastructure
```bash

docker-compose up -d

cd config-server
./mvnw spring-boot:run

cd eureka-server
./mvnw spring-boot:run

cd user-service
./mvnw spring-boot:run

cd game-service
./mvnw spring-boot:run

cd leaderboard-service
./mvnw spring-boot:run

cd api-gateway
./mvnw spring-boot:run