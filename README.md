# Life Momentum 360

A connected personal life management platform — goals, tasks, habits, calendar, finance, documents, and learning
feed into one system instead of living as separate unrelated trackers. Goal progress drives task creation, habits
support goal execution, calendar protects the time needed to hit deadlines, and a rule-based insight engine flags
when something is falling behind — all without relying on an external AI API.

## Repo Structure

```
life-momentum-360/
├── lifesync-backend/     # Java 21 + Spring Boot (modular monolith) — REST API
├── lifesync-frontend/    # React — dashboard, forms, charts (built after backend is complete)
└── README.md
```

## Status

- [x] Stage 1 — Project setup + Identity/Auth (JWT access + refresh tokens, register/login/refresh/logout)
- [ ] Stage 2 — Life Areas + Dashboard skeleton
- [ ] Stage 3 — Goals + Milestones + Goal Health
- [ ] Stage 4 — Tasks + recurring tasks + dependencies
- [ ] Stage 5 — Habits + Routines + streaks
- [ ] Stage 6 — Calendar + Time Blocks + conflict detection
- [ ] Stage 7 — Finance + Budgets
- [ ] Stage 8 — Documents + Learning
- [ ] Stage 9 — Reminders + Notification scheduler
- [ ] Stage 10 — Analytics + Insight Engine
- [ ] Stage 11 — Weekly Review + Export + Audit
- [ ] Stage 12 — Security hardening, testing, deployment

Backend is being built and tested stage-by-stage in Postman before the frontend begins.
See `lifesync-backend/README.md` for setup and API testing instructions for the current stage.

## Tech Stack

**Backend:** Java 21, Spring Boot 3, Spring Security, JWT, Spring Data JPA/Hibernate, MySQL, Bean Validation, Spring Scheduler
**Frontend:** React, React Router, Axios, Recharts
**Testing:** Postman (API-level, per stage), JUnit/Mockito (unit, added at Stage 12)
