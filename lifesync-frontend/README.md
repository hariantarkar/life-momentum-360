# LifeSync Frontend — Stage 1: Project Setup + Auth

## What's included in this stage
- Vite + React 18 project setup
- Bootstrap 5 (plain CSS classes, no component library) + custom CSS per component
- Axios instance with automatic access-token attachment and auto-refresh-on-401 logic
- AuthContext: login, register, logout, session persistence via localStorage
- Protected routes (redirect to `/login` if not authenticated)
- Pages: Login, Register, and a minimal Dashboard placeholder proving the auth flow works end to end
- Full folder structure scaffolded for all 12 stages ahead (matching the backend 1:1), so each future stage has a home to drop into

## Folder Structure

```
lifesync-frontend/
├── public/
├── src/
│   ├── api/
│   │   └── axiosInstance.js       # Axios + auto-refresh interceptor
│   ├── components/
│   │   ├── layout/
│   │   │   ├── Navbar.jsx
│   │   │   └── Navbar.css
│   │   └── common/
│   │       ├── LoadingSpinner.jsx
│   │       └── LoadingSpinner.css
│   ├── routes/
│   │   ├── AppRoutes.jsx
│   │   └── ProtectedRoute.jsx
│   ├── features/
│   │   ├── auth/                  # <-- Stage 1 (built)
│   │   │   ├── components/
│   │   │   ├── context/AuthContext.jsx
│   │   │   ├── pages/LoginPage.jsx, RegisterPage.jsx, AuthPages.css
│   │   │   └── services/authService.js
│   │   ├── dashboard/             # <-- Stage 1 placeholder, built out in Stage 2
│   │   ├── lifearea/               } 
│   │   ├── goal/                   } scaffolded, empty —
│   │   ├── task/                   } built out stage by stage
│   │   ├── habit/                  } to match the backend
│   │   ├── calendar/                }
│   │   ├── finance/                 }
│   │   ├── document/                }
│   │   ├── learning/                }
│   │   ├── notification/            }
│   │   ├── analytics/               }
│   │   └── review/                  }
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── index.html
├── vite.config.js
├── package.json
├── .env                            # not committed — see .env.example
└── .env.example
```

Each feature folder follows the same `pages/ components/ services/` pattern, so once you're
in the rhythm of one stage, every future stage looks the same shape.

## Setup

1. **Install dependencies:**
   ```bash
   cd lifesync-frontend
   npm install
   ```

2. **Configure the API URL** — `.env` is already set to point at your local backend:
   ```
   VITE_API_BASE_URL=http://localhost:8080/api
   ```
   Change this if your backend runs on a different port, or to your deployed Render URL
   once you're testing against production.

3. **Make sure your backend is running** (`mvn spring-boot:run` in `lifesync-backend/`, or your
   STS run configuration) — the frontend has nothing to talk to otherwise.

4. **Run the dev server:**
   ```bash
   npm run dev
   ```
   Opens at `http://localhost:3000`.

## Manual Test Flow

1. Go to `http://localhost:3000` — should redirect you to `/login` since you're not authenticated.
2. Click "Sign up" → fill in the registration form → submit. Should show a success message and
   redirect to `/login` after ~1.5 seconds.
3. Log in with the account you just created. Should redirect to `/dashboard`, showing your name
   and email.
4. Refresh the page while on `/dashboard` — you should stay logged in (session persists via
   localStorage), not get bounced back to login.
5. Click "Logout" in the navbar — should clear your session and redirect to `/login`. Try
   navigating directly to `/dashboard` in the URL bar afterward — should redirect you back to
   `/login` since you're no longer authenticated.
6. **Token refresh test:** this is harder to trigger manually since access tokens last 15 minutes,
   but if you want to verify it works, you can temporarily lower `access-token-expiration-ms` in
   the backend's `application.yml` to something like `30000` (30 seconds) for testing, log in,
   wait 35 seconds, then make any API call (e.g. reload `/dashboard`, or once Stage 2 exists,
   any actual API-backed page) — it should refresh silently in the background and the request
   should still succeed, with no visible interruption or redirect to login. Remember to change
   the expiration back afterward.

## Next Stage

Once this all works cleanly, we move to **Frontend Stage 2: Life Areas + Dashboard**, wiring up
the real dashboard against `GET /api/dashboard` and building out life area CRUD — matching backend
Stage 2.
