# Transformation Service

Two Spring Boot microservices with JWT auth and inter-service communication.

## Architecture

- **auth-api** (`:8080`) — registration, login, protected `/api/process` endpoint. Connects to PostgreSQL.
- **data-api** (`:8081`) — text transformation. Accepts requests only from `auth-api` via `X-Internal-Token` header.
- **postgres** — single database with `users` and `processing_log` tables.

## Prerequisites

- Docker & Docker Compose
- Java 21 + Maven (only if building locally outside Docker)

---

## 1. Clone the repository

```bash
git clone https://github.com/o-morenets/transformation.git
cd transformation
```

---

## 2. Prepare `.env`

Copy the example file:

```bash
cp .env.example .env
```

Then edit `.env`:

```env
POSTGRES_USER=winwin
POSTGRES_PASSWORD=changeme

POSTGRES_URL=jdbc:postgresql://postgres:5432/winwin

# Generate with: openssl rand -base64 64
JWT_SECRET=your-very-long-secret-here

# Generate with: openssl rand -base64 24
INTERNAL_TOKEN=your-internal-token-here
```

Generate secrets directly from terminal:

```bash
# JWT_SECRET (min 32 chars required for HMAC-SHA256)
openssl rand -base64 64

# INTERNAL_TOKEN
openssl rand -base64 24
```

> ⚠️ Never commit `.env` to git. It is listed in `.gitignore`.

---

## 3. Build and run

```bash
docker compose up -d --build
```

Check that all three containers are running:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f auth-api
docker compose logs -f data-api
```

---

## 4. Test the flow

### Register

```bash
curl.exe -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{\"email\":\"a@a.com\",\"password\":\"password123\"}'
```

Expected: `201 Created`

### Login

```bash
curl.exe -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{\"email\":\"a@a.com\",\"password\":\"password123\"}'
```

Expected:
```json
{ "token": "<jwt-token>" }
```

### Process (protected endpoint)

```bash
curl.exe -X POST http://localhost:8080/api/process -H "Authorization: Bearer <jwt-token>" -H "Content-Type: application/json" -d '{\"text\":\"hello\"}'
```

Expected:
```json
{ "result": "OLLEH" }
```

A row is saved to `processing_log` in PostgreSQL.

### Verify Service B rejects unauthorized requests

```bash
curl.exe -X POST http://localhost:8081/api/transform -H "Content-Type: application/json" -d '{\"text\":\"hello\"}'
```

Expected: `403 Forbidden`

---

## 5. Verify database records

```bash
docker compose exec postgres psql -U winwin -d winwin -c "SELECT * FROM processing_log;"
```

---

## 6. Stop

```bash
docker compose down
```

To also remove the database volume:

```bash
docker compose down -v
```
