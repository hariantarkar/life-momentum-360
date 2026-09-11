import { useState } from 'react';
import './MilestoneForm.css';

function MilestoneForm({ onAdd, submitting }) {
  const [title, setTitle] = useState('');
  const [targetDate, setTargetDate] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    if (!title.trim()) return;
    onAdd({ title, targetDate: targetDate || null, displayOrder: 0 });
    setTitle('');
    setTargetDate('');
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-milestone-form">
      <input
        type="text"
        className="form-control"
        placeholder="Add a milestone..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <input
        type="date"
        className="form-control lifesync-milestone-date-input"
        value={targetDate}
        onChange={(e) => setTargetDate(e.target.value)}
      />
      <button type="submit" className="btn btn-outline-primary" disabled={submitting}>
        Add
      </button>
    </form>
  );
}

export default MilestoneForm;