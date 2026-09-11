import { Link } from 'react-router-dom';
import HealthBadge from './HealthBadge';
import './GoalCard.css';

function GoalCard({ goal }) {
  return (
    <Link to={`/goals/${goal.id}`} className="lifesync-goal-card">
      <div className="d-flex justify-content-between align-items-start mb-2">
        <h5 className="lifesync-goal-card-title">{goal.title}</h5>
        <HealthBadge health={goal.health} />
      </div>

      {goal.lifeAreaName && <div className="lifesync-goal-card-area">{goal.lifeAreaName}</div>}

      <div className="lifesync-goal-progress-wrapper">
        <div className="progress lifesync-goal-progress">
          <div
            className="progress-bar"
            role="progressbar"
            style={{ width: `${goal.progressPercentage}%` }}
            aria-valuenow={goal.progressPercentage}
            aria-valuemin="0"
            aria-valuemax="100"
          />
        </div>
        <span className="lifesync-goal-progress-label">
          {goal.progressPercentage}% · {goal.completedMilestones}/{goal.totalMilestones} milestones
        </span>
      </div>

      <div className="lifesync-goal-card-footer">Target: {goal.targetDate}</div>
    </Link>
  );
}

export default GoalCard;