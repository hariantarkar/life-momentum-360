import { useState, useEffect } from 'react';
import {
  getHabits,
  createHabit,
  updateHabit,
  deleteHabit,
  logHabitToday,
  unlogHabitToday,
} from '../services/habitService';
import HabitCard from '../components/HabitCard';
import HabitForm from '../components/HabitForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './HabitsPage.css';

function HabitsPage() {
  const [habits, setHabits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingHabit, setEditingHabit] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadHabits();
  }, []);

  async function loadHabits() {
    setLoading(true);
    setError('');
    try {
      const data = await getHabits();
      setHabits(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load habits.');
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(payload) {
    setSubmitting(true);
    try {
      await createHabit(payload);
      setShowForm(false);
      await loadHabits();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create habit.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleUpdate(payload) {
    setSubmitting(true);
    try {
      await updateHabit(editingHabit.id, payload);
      setEditingHabit(null);
      await loadHabits();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update habit.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleLog(habitId) {
    try {
      await logHabitToday(habitId);
      await loadHabits();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to log habit.');
    }
  }

  async function handleUnlog(habitId) {
    try {
      await unlogHabitToday(habitId);
      await loadHabits();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to undo log.');
    }
  }

  async function handleDelete(habit) {
    if (!window.confirm(`Delete "${habit.title}"? Its streak history will be kept but hidden.`)) return;
    try {
      await deleteHabit(habit.id);
      await loadHabits();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete habit.');
    }
  }

  return (
    <div className="container lifesync-habits-page">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="lifesync-page-title">Habits</h1>
        {!showForm && !editingHabit && (
          <button className="btn btn-primary" onClick={() => setShowForm(true)}>
            + New Habit
          </button>
        )}
      </div>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {showForm && (
        <HabitForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} submitting={submitting} />
      )}

      {editingHabit && (
        <HabitForm
          initialData={editingHabit}
          onSubmit={handleUpdate}
          onCancel={() => setEditingHabit(null)}
          submitting={submitting}
        />
      )}

      {loading ? (
        <LoadingSpinner label="Loading habits..." />
      ) : habits.length === 0 ? (
        <p className="text-muted">
          No habits yet. Add a daily or weekly habit and start building your streak.
        </p>
      ) : (
        habits.map((habit) => (
          <HabitCard
            key={habit.id}
            habit={habit}
            onLog={handleLog}
            onUnlog={handleUnlog}
            onEdit={setEditingHabit}
            onDelete={handleDelete}
          />
        ))
      )}
    </div>
  );
}

export default HabitsPage;