import PriorityBadge from './PriorityBadge';
import './TaskItem.css';

function TaskItem({ task, onToggleComplete, onEdit, onDelete }) {
  const isDone = task.status === 'DONE';
  const isBlocked = task.dependsOnTaskId && !task.dependsOnCompleted && !isDone;

  return (
    <div className={`lifesync-task-item ${task.overdue ? 'lifesync-task-overdue' : ''}`}>
      <div className="form-check lifesync-task-checkbox">
        <input
          type="checkbox"
          className="form-check-input"
          checked={isDone}
          disabled={isDone || isBlocked}
          onChange={() => onToggleComplete(task)}
          title={isBlocked ? `Blocked by: ${task.dependsOnTaskTitle}` : ''}
        />
      </div>

      <div className="lifesync-task-body">
        <div className="d-flex align-items-center gap-2 flex-wrap">
          <span className={`lifesync-task-title ${isDone ? 'lifesync-task-title-done' : ''}`}>
            {task.title}
          </span>
          <PriorityBadge priority={task.priority} />
          {task.overdue && <span className="badge bg-danger">Overdue</span>}
          {task.recurring && <span className="badge bg-info text-dark">Recurring</span>}
          {isBlocked && (
            <span className="badge bg-secondary" title={`Blocked by: ${task.dependsOnTaskTitle}`}>
              Blocked
            </span>
          )}
        </div>

        <div className="lifesync-task-meta">
          {task.dueDate && <span>Due: {task.dueDate}</span>}
          {task.goalTitle && <span>Goal: {task.goalTitle}</span>}
          {task.estimatedMinutes && <span>{task.estimatedMinutes} min</span>}
        </div>
      </div>

      <div className="lifesync-task-actions">
        <button className="btn btn-sm btn-outline-secondary" onClick={() => onEdit(task)}>
          Edit
        </button>
        <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(task)}>
          Delete
        </button>
      </div>
    </div>
  );
}

export default TaskItem;