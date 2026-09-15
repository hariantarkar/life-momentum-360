import { useState, useEffect } from 'react';
import { getGoals } from '../../goal/services/goalService';
import './HabitForm.css';

function HabitForm({ initialData, onSubmit, onCancel, submitting }) {
  const [title, setTitle] = useState(initialData?.title || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [goalId, setGoalId] = useState(initialData?.goalId || '');
  const [frequency, setFrequency] = useState(initialData?.frequency || 'DAILY');
  const [goals, setGoals] = useState([]);

  useEffect(() => {
    getGoals().then(setGoals).catch(() => setGoals([]));
  }, []);

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({
      title,
      description,
      goalId: goalId ? Number(goalId) : null,
      frequency,
    });
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-habit-form">
      <div className="mb-3">
        <label htmlFor="habitTitle" className="form-label">Title</label>
        <input
          type="text"
          className="form-control"
          id="habitTitle"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="e.g. Morning meditation, Daily coding practice"
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="habitDescription" className="form-label">
          Description <span className="text-muted">(optional)</span>
        </label>
        <textarea
          className="form-control"
          id="habitDescription"
          rows="2"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <div className="row">
        <div className="col-md-6 mb-3">
          <label htmlFor="habitFrequency" className="form-label">Frequency</label>
          <select
            className="form-select"
            id="habitFrequency"
            value={frequency}
            onChange={(e) => setFrequency(e.target.value)}
          >
            <option value="DAILY">Daily</option>
            <option value="WEEKLY">Weekly</option>
          </select>
        </div>

        <div className="col-md-6 mb-3">
          <label htmlFor="habitGoal" className="form-label">
            Supports Goal <span className="text-muted">(optional)</span>
          </label>
          <select
            className="form-select"
            id="habitGoal"
            value={goalId}
            onChange={(e) => setGoalId(e.target.value)}
          >
            <option value="">None</option>
            {goals.map((g) => (
              <option key={g.id} value={g.id}>{g.title}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="d-flex gap-2">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : initialData ? 'Save Changes' : 'Create Habit'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default HabitForm;