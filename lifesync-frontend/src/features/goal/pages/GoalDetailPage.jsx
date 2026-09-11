import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getGoal, updateGoal, markGoalComplete, deleteGoal } from '../services/goalService';
import { createMilestone, toggleMilestoneComplete, deleteMilestone } from '../services/milestoneService';
import HealthBadge from '../components/HealthBadge';
import GoalForm from '../components/GoalForm';
import MilestoneList from '../components/MilestoneList';
import MilestoneForm from '../components/MilestoneForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './GoalDetailPage.css';

function GoalDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [goal, setGoal] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadGoal();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  async function loadGoal() {
    setLoading(true);
    setError('');
    try {
      const data = await getGoal(id);
      setGoal(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load this goal.');
    } finally {
      setLoading(false);
    }
  }

  async function handleUpdate(payload) {
    setSubmitting(true);
    try {
      await updateGoal(id, payload);
      setEditing(false);
      await loadGoal();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update goal.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleMarkComplete() {
    try {
      await markGoalComplete(id);
      await loadGoal();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to mark goal complete.');
    }
  }

  async function handleDeleteGoal() {
    if (!window.confirm(`Delete "${goal.title}" and all its milestones? This can't be undone.`)) {
      return;
    }
    try {
      await deleteGoal(id);
      navigate('/goals');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete goal.');
    }
  }

  async function handleAddMilestone(payload) {
    try {
      await createMilestone(id, payload);
      await loadGoal();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add milestone.');
    }
  }

  async function handleToggleMilestone(milestoneId) {
    try {
      await toggleMilestoneComplete(id, milestoneId);
      await loadGoal();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update milestone.');
    }
  }

  async function handleDeleteMilestone(milestoneId) {
    try {
      await deleteMilestone(id, milestoneId);
      await loadGoal();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to remove milestone.');
    }
  }

  if (loading) {
    return <LoadingSpinner label="Loading goal..." />;
  }

  if (error && !goal) {
    return (
      <div className="container lifesync-goal-detail">
        <div className="alert alert-danger">{error}</div>
        <Link to="/goals">&larr; Back to Goals</Link>
      </div>
    );
  }

  return (
    <div className="container lifesync-goal-detail">
      <Link to="/goals" className="lifesync-back-link">
        &larr; Back to Goals
      </Link>

      {error && (
        <div className="alert alert-danger mt-3" role="alert">
          {error}
        </div>
      )}

      {editing ? (
        <GoalForm
          initialData={goal}
          onSubmit={handleUpdate}
          onCancel={() => setEditing(false)}
          submitting={submitting}
        />
      ) : (
        <>
          <div className="d-flex justify-content-between align-items-start mt-3 mb-2">
            <h1 className="lifesync-goal-detail-title">{goal.title}</h1>
            <HealthBadge health={goal.health} />
          </div>

          {goal.lifeAreaName && <div className="lifesync-goal-detail-area">{goal.lifeAreaName}</div>}
          {goal.description && <p className="lifesync-goal-detail-description">{goal.description}</p>}

          <div className="lifesync-goal-detail-meta">
            <span>Target: {goal.targetDate}</span>
            <span>Status: {goal.status}</span>
            <span>Progress: {goal.progressPercentage}%</span>
          </div>

          <div className="progress lifesync-goal-detail-progress">
            <div
              className="progress-bar"
              role="progressbar"
              style={{ width: `${goal.progressPercentage}%` }}
            />
          </div>

          <div className="d-flex gap-2 mt-3 mb-4">
            <button className="btn btn-outline-secondary btn-sm" onClick={() => setEditing(true)}>
              Edit
            </button>
            {goal.status !== 'COMPLETED' && (
              <button className="btn btn-success btn-sm" onClick={handleMarkComplete}>
                Mark Complete
              </button>
            )}
            <button className="btn btn-outline-danger btn-sm" onClick={handleDeleteGoal}>
              Delete Goal
            </button>
          </div>

          <hr />

          <h2 className="lifesync-section-title">Milestones</h2>
          <MilestoneList
            milestones={goal.milestones || []}
            onToggle={handleToggleMilestone}
            onDelete={handleDeleteMilestone}
          />
          <MilestoneForm onAdd={handleAddMilestone} />
        </>
      )}
    </div>
  );
}

export default GoalDetailPage;