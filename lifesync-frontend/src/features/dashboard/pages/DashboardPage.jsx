import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getDashboard } from '../services/dashboardService';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './DashboardPage.css';

function StatCard({ label, value }) {
  return (
    <div className="col-sm-6 col-lg-3 mb-3">
      <div className="lifesync-stat-card">
        <div className="lifesync-stat-value">{value}</div>
        <div className="lifesync-stat-label">{label}</div>
      </div>
    </div>
  );
}

function DashboardPage() {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadDashboard();
  }, []);

  async function loadDashboard() {
    setLoading(true);
    setError('');
    try {
      const data = await getDashboard();
      setDashboard(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load your dashboard.');
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <LoadingSpinner label="Loading your dashboard..." />;
  }

  if (error) {
    return (
      <div className="container lifesync-dashboard">
        <div className="alert alert-danger">{error}</div>
      </div>
    );
  }

  return (
    <div className="container lifesync-dashboard">
      <h1 className="lifesync-dashboard-title">Welcome back, {dashboard.fullName?.split(' ')[0]}</h1>
      <p className="lifesync-dashboard-subtitle">{dashboard.email}</p>

      <div className="row mt-4">
        <StatCard label="Life Areas" value={dashboard.totalLifeAreas} />
        <StatCard label="Active Goals" value={dashboard.activeGoalsCount} />
        <StatCard label="Tasks Due Today" value={dashboard.tasksDueToday} />
        <StatCard label="Habits Pending" value={dashboard.habitsPendingToday} />
      </div>

      {dashboard.note && (
        <div className="lifesync-dashboard-note">
          <small>{dashboard.note}</small>
        </div>
      )}

      <div className="lifesync-dashboard-section">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h2 className="lifesync-section-title">Your Life Areas</h2>
          <Link to="/life-areas" className="btn btn-sm btn-outline-primary">
            Manage Life Areas
          </Link>
        </div>

        {dashboard.lifeAreas.length === 0 ? (
          <p className="text-muted">
            You haven't created any life areas yet.{' '}
            <Link to="/life-areas">Create your first one</Link> to start organizing goals, tasks,
            and habits.
          </p>
        ) : (
          <div className="lifesync-lifearea-chips">
            {dashboard.lifeAreas.map((la) => (
              <span
                key={la.id}
                className="lifesync-lifearea-chip"
                style={{ borderColor: la.colorCode || '#9ca3af' }}
              >
                {la.name}
              </span>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default DashboardPage;