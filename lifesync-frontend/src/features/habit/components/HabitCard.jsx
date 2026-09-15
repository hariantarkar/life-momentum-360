import './HabitCard.css';

function HabitCard({ habit, onLog, onUnlog, onEdit, onDelete }) {
  const periodLabel = habit.frequency === 'WEEKLY' ? 'this week' : 'today';

  return (
    <div className="lifesync-habit-card">
      <div className="lifesync-habit-card-main">
        <div className="d-flex align-items-center gap-2 flex-wrap">
          <h5 className="lifesync-habit-card-title">{habit.title}</h5>
          <span className="badge bg-light text-dark border">{habit.frequency}</span>
        </div>

        {habit.goalTitle && <div className="lifesync-habit-card-goal">Supports: {habit.goalTitle}</div>}

        <div className="lifesync-habit-stats">
          <div className="lifesync-habit-streak">
            <span className="lifesync-habit-streak-flame">🔥</span>
            <span className="lifesync-habit-streak-count">{habit.currentStreak}</span>
            <span className="lifesync-habit-streak-label">day streak</span>
          </div>

          <div className="lifesync-habit-adherence">
            <div className="progress lifesync-habit-adherence-bar">
              <div
                className="progress-bar bg-success"
                role="progressbar"
                style={{ width: `${habit.adherencePercentage}%` }}
              />
            </div>
            <span className="lifesync-habit-adherence-label">
              {habit.adherencePercentage}% adherence
            </span>
          </div>
        </div>
      </div>

      <div className="lifesync-habit-card-actions">
        {habit.loggedForCurrentPeriod ? (
          <div className="text-center">
            <span className="badge bg-success mb-1 d-block">✓ Logged {periodLabel}</span>
            <button className="btn btn-sm btn-link text-muted p-0" onClick={() => onUnlog(habit.id)}>
              Undo
            </button>
          </div>
        ) : (
          <button className="btn btn-success btn-sm" onClick={() => onLog(habit.id)}>
            Log {periodLabel}
          </button>
        )}

        <div className="d-flex gap-2 mt-2">
          <button className="btn btn-sm btn-outline-secondary" onClick={() => onEdit(habit)}>
            Edit
          </button>
          <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(habit)}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default HabitCard;