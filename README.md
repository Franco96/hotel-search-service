# Search Service

REST API built with Spring Boot 3 and Java 21 to register and count identical search queries.

---

### Prerequisites
- **Java 21** (required to run or test locally without Docker).
- **Docker** (required to run the full application).

---

### Setup (Docker)
The infrastructure and application are fully dockerized.

```bash
# Start the full stack
docker compose up --build

# Stop all services
docker compose down

```
Note: Database initialization may take a few minutes on the first run. The app will wait for all services to be ready.

---

### Documentation (Swagger)
Interactive API documentation:
http://localhost:8081/swagger-ui.html

---

### Endpoints

1. **POST /search**: Registers a new search and returns a unique identifier assigned to the search.

2. **GET /count**: Returns the number of times an identical search has been performed.
---

### Architecture & Tech
- Hexagonal Architecture (Domain, Application, and Infrastructure layers).
- Java 21: Virtual Threads for efficient consumer processing.
- SHA-256: Unique hash generation for duplicate identification.
- Persistence: Oracle 23c.
- Quality: Jacoco code coverage > 80%.

---

### Tests
To run tests locally and generate the coverage report:
```bash
./mvnw clean verify
```
Report location: `target/site/jacoco/index.html`
