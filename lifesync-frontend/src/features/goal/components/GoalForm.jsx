import { useState, useEffect } from 'react';
import { getLifeAreas } from '../../lifearea/services/lifeAreaService';
import './GoalForm.css';

function GoalForm({ initialData, onSubmit, onCancel, submitting }) {
  const [title, setTitle] = useState(initialData?.title || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [lifeAreaId, setLifeAreaId] = useState(initialData?.lifeAreaId || '');
  const [targetDate, setTargetDate] = useState(initialData?.targetDate || '');
  const [lifeAreas, setLifeAreas] = useState([]);

  useEffect(() => {
    getLifeAreas()
      .then(setLifeAreas)
      .catch(() => setLifeAreas([]));
  }, []);

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({
      title,
      description,
      lifeAreaId: lifeAreaId ? Number(lifeAreaId) : null,
      targetDate,
    });
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-goal-form">
      <div className="mb-3">
        <label htmlFor="goalTitle" className="form-label">Title</label>
        <input
          type="text"
          className="form-control"
          id="goalTitle"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="goalDescription" className="form-label">
          Description <span className="text-muted">(optional)</span>
        </label>
        <textarea
          className="form-control"
          id="goalDescription"
          rows="2"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <div className="mb-3">
        <label htmlFor="goalLifeArea" className="form-label">
          Life Area <span className="text-muted">(optional)</span>
        </label>
        <select
          className="form-select"
          id="goalLifeArea"
          value={lifeAreaId}
          onChange={(e) => setLifeAreaId(e.target.value)}
        >
          <option value="">None</option>
          {lifeAreas.map((la) => (
            <option key={la.id} value={la.id}>
              {la.name}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-3">
        <label htmlFor="goalTargetDate" className="form-label">Target Date</label>
        <input
          type="date"
          className="form-control"
          id="goalTargetDate"
          value={targetDate}
          onChange={(e) => setTargetDate(e.target.value)}
          required
        />
      </div>

      <div className="d-flex gap-2">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : initialData ? 'Save Changes' : 'Create Goal'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default GoalForm;