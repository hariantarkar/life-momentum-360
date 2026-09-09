import { useAuth } from '../../auth/context/AuthContext';
import './DashboardPage.css';

/**
 * Minimal placeholder for Frontend Stage 1 — just proves the protected route
 * and auth flow work end to end. The real dashboard (life areas, goal/task/
 * habit widgets) gets built out in Frontend Stage 2, mirroring backend Stage 2.
 */
function DashboardPage() {
  const { user } = useAuth();

  return (
    <div className="container lifesync-dashboard">
      <h1 className="lifesync-dashboard-title">Welcome, {user?.fullName}</h1>
      <p className="lifesync-dashboard-subtitle">{user?.email}</p>

      <div className="lifesync-dashboard-placeholder">
        <p>
          🎉 You're logged in and this page is protected — the auth flow is working.
        </p>
        <p className="text-muted">
          The real dashboard (life areas, goal progress, today's tasks, habit streaks)
          gets built here in the next stage.
        </p>
      </div>
    </div>
  );
}

export default DashboardPage;
