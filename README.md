# LifeSync Backend — Stage 1: Project Setup + Auth

## What's included in this stage
- Spring Boot 3 / Java 21 project skeleton (modular monolith, domain-packaged)
- MySQL + Spring Data JPA config
- JWT authentication: access token (JWT, 15 min) + refresh token (opaque, DB-stored, 7 days, rotated on use)
- Endpoints: `register`, `login`, `refresh`, `logout`, `me` (protected)
- Global exception handling with a consistent `ApiResponse<T>` envelope
- Empty scaffolded folders for every future module (goal, task, habit, calendar, finance, document, learning, notification, analytics, review, audit) so the structure stays consistent stage to stage

## Setup

1. **Create the database** — MySQL will auto-create it on first run (`createDatabaseIfNotExist=true`), but MySQL server must be running locally on port 3306.
2. **Edit** `src/main/resources/application.yml`:
   - `spring.datasource.username` / `password` — your MySQL credentials
   - `lifesync.jwt.secret` — replace with your own long random string before you ever deploy this anywhere real
3. **Run:**
   ```bash
   cd lifesync-backend
   mvn spring-boot:run
   ```
   Server starts on `http://localhost:8080`.

4. Swagger UI (optional, handy alongside Postman): `http://localhost:8080/swagger-ui.html`

## Postman Test Flow (do these in order)

### 1. Register
`POST http://localhost:8080/api/auth/register`
```json
{
  "fullName": "Rahul Sharma",
  "email": "rahul@example.com",
  "password": "SecurePass123"
}
```
Expect `201 Created` with the created user (no password in response).

### 2. Login
`POST http://localhost:8080/api/auth/login`
```json
{
  "email": "rahul@example.com",
  "password": "SecurePass123"
}
```
Expect `200 OK` with `accessToken`, `refreshToken`, and user info.
→ **Save both tokens** as Postman environment variables (`accessToken`, `refreshToken`) so you can reuse them.

### 3. Hit a protected route
`GET http://localhost:8080/api/auth/me`
Header: `Authorization: Bearer {{accessToken}}`
Expect `200 OK` with your id/email/authorities. Try it **without** the header too — should get `401/403`.

### 4. Refresh
`POST http://localhost:8080/api/auth/refresh`
```json
{ "refreshToken": "{{refreshToken}}" }
```
Expect a brand-new access + refresh token pair. The old refresh token is now revoked (try reusing it — should fail).

### 5. Logout
`POST http://localhost:8080/api/auth/logout`
```json
{ "refreshToken": "{{refreshToken}}" }
```
Expect success. Try `/refresh` again with that same token afterward — should be rejected.

### Error cases worth testing
- Register with an already-used email → `400`
- Register with a password under 8 chars → `400` with field-level validation message
- Login with wrong password → `401`
- Any protected route with an expired/garbage token → `403`

## Next Stage
Once all of the above pass cleanly in Postman, we move to **Stage 2: Life Areas + Dashboard skeleton**. Say the word and I'll build it in the same pattern (entity → repository → service → controller → DTOs).
