import './MilestoneList.css';

function MilestoneList({ milestones, onToggle, onDelete }) {
  if (milestones.length === 0) {
    return <p className="text-muted">No milestones yet. Break this goal down into steps below.</p>;
  }

  return (
    <ul className="lifesync-milestone-list">
      {milestones.map((m) => (
        <li key={m.id} className="lifesync-milestone-item">
          <div className="form-check">
            <input
              type="checkbox"
              className="form-check-input"
              id={`milestone-${m.id}`}
              checked={m.completed}
              onChange={() => onToggle(m.id)}
            />
            <label
              htmlFor={`milestone-${m.id}`}
              className={`form-check-label ${m.completed ? 'lifesync-milestone-completed' : ''}`}
            >
              {m.title}
            </label>
          </div>
          {m.targetDate && <span className="lifesync-milestone-date">{m.targetDate}</span>}
          <button
            type="button"
            className="btn btn-sm btn-link text-danger lifesync-milestone-delete"
            onClick={() => onDelete(m.id)}
          >
            Remove
          </button>
        </li>
      ))}
    </ul>
  );
}

export default MilestoneList;