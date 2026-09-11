import { useState, useEffect } from 'react';
import { getGoals, createGoal } from '../services/goalService';
import GoalCard from '../components/GoalCard';
import GoalForm from '../components/GoalForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './GoalsPage.css';

function GoalsPage() {
  const [goals, setGoals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadGoals();
  }, []);

  async function loadGoals() {
    setLoading(true);
    setError('');
    try {
      const data = await getGoals();
      setGoals(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load goals.');
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(payload) {
    setSubmitting(true);
    try {
      await createGoal(payload);
      setShowForm(false);
      await loadGoals();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create goal.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="container lifesync-goals-page">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="lifesync-page-title">Goals</h1>
        {!showForm && (
          <button className="btn btn-primary" onClick={() => setShowForm(true)}>
            + New Goal
          </button>
        )}
      </div>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {showForm && (
        <GoalForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} submitting={submitting} />
      )}

      {loading ? (
        <LoadingSpinner label="Loading goals..." />
      ) : goals.length === 0 ? (
        <p className="text-muted">
          No goals yet. Set your first long-term goal and break it into milestones.
        </p>
      ) : (
        goals.map((goal) => <GoalCard key={goal.id} goal={goal} />)
      )}
    </div>
  );
}

export default GoalsPage;