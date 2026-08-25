# Deployment Guide

## Environment Variables

Every secret and environment-specific value is externalized — nothing sensitive is hardcoded
in `application.yml` (dev defaults only, safe to commit) or `application-prod.yml` (no defaults
at all for secrets, forcing the app to fail fast on startup if something is missing rather than
silently running with a placeholder in production).

| Variable | Required in prod? | Example |
|---|---|---|
| `DB_URL` | Yes | `jdbc:mysql://your-db-host:3306/lifesync_db?useSSL=true&serverTimezone=UTC` |
| `DB_USERNAME` | Yes | `lifesync_app` |
| `DB_PASSWORD` | Yes | *(strong generated password)* |
| `JWT_SECRET` | Yes | 32+ character random string — see below |
| `JWT_ACCESS_EXPIRATION_MS` | No (defaults to 900000 / 15 min) | `900000` |
| `JWT_REFRESH_EXPIRATION_MS` | No (defaults to 604800000 / 7 days) | `604800000` |
| `CORS_ALLOWED_ORIGINS` | Yes | `https://your-frontend-domain.com` |
| `SERVER_PORT` | No (most platforms set this automatically) | `8080` |

**Generate a production JWT secret:**
```bash
python3 -c "import secrets; print(secrets.token_urlsafe(48))"
```
Never reuse the development secret from `application.yml` in production — treat it as already
compromised the moment it's in a public repo.

---

## Option A: Run Locally with Docker Compose (easiest way to prove it works)

This spins up both the app and a real MySQL container together — good for a final local sanity
check before deploying anywhere, and good to show in a portfolio demo/interview.

```bash
cd lifesync-backend
docker compose up --build
```

The app will be reachable at `http://localhost:8080`. This uses `docker-compose.yml`'s built-in
dev credentials (not for real deployment — see the warning comment at the top of that file).

To stop and remove the containers:
```bash
docker compose down
```

To also wipe the database volume (fresh start):
```bash
docker compose down -v
```

---

## Option B: Build and Run the Docker Image Directly

```bash
cd lifesync-backend
docker build -t lifesync-backend .

docker run -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/lifesync_db?useSSL=false&serverTimezone=UTC" \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=your_password \
  -e JWT_SECRET=your_generated_secret \
  -e CORS_ALLOWED_ORIGINS=http://localhost:3000 \
  -e SPRING_PROFILES_ACTIVE=prod \
  lifesync-backend
```

`host.docker.internal` lets the container reach a MySQL instance running on your host machine
(works on Docker Desktop for Mac/Windows; on Linux use `--network host` or point to a real DB host instead).

---

## Option C: Deploy to a Free-Tier Cloud Host (Render, Railway, etc.)

The general pattern is the same across most platforms:

1. Push this repo to GitHub (already done).
2. Create a new "Web Service" and point it at the `lifesync-backend/` folder (most platforms let
   you set a build context/root directory if the Dockerfile isn't at the repo root).
3. Let the platform build from the `Dockerfile` directly — no extra build configuration needed.
4. Provision a managed MySQL database (most platforms offer this, or use a separate free-tier
   MySQL host like PlanetScale/Aiven) and get its connection URL.
5. Set the environment variables from the table above in the platform's dashboard — this is
   where `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, and `CORS_ALLOWED_ORIGINS` actually
   get their real values. Also set `SPRING_PROFILES_ACTIVE=prod`.
6. Deploy. Watch the logs for `Started LifeSyncApplication` and confirm `Hibernate: ... validate`
   ran cleanly (in prod mode, Hibernate checks the schema matches instead of auto-creating it —
   if this is a brand new database, temporarily set `SPRING_PROFILES_ACTIVE` to unset/default
   for the very first deploy so `ddl-auto: update` can create the tables, then switch to `prod`
   for all subsequent deploys).

Exact steps (button names, dashboard layout) vary by platform and change over time — the
environment variable list above is the part that stays constant regardless of where you deploy.

---

## Pre-Deployment Checklist

- [ ] `JWT_SECRET` is a freshly generated value, not the one from local `application.yml`
- [ ] `DB_PASSWORD` is a strong generated password, not `root`/`root`
- [ ] `CORS_ALLOWED_ORIGINS` is set to your actual frontend domain, not `localhost`
- [ ] `SPRING_PROFILES_ACTIVE=prod` is set (activates `application-prod.yml`: `ddl-auto: validate`,
      no SQL logging, Swagger disabled, no stack traces in error responses)
- [ ] Database already has the schema created (first deploy without `prod` profile, or run
      migrations manually) before switching to `validate` mode
- [ ] `mvn test` passes locally before pushing