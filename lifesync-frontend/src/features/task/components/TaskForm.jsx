import { useState, useEffect } from 'react';
import { getGoals } from '../../goal/services/goalService';
import { getTasks } from '../services/taskService';
import './TaskForm.css';

function TaskForm({ initialData, onSubmit, onCancel, submitting }) {
  const [title, setTitle] = useState(initialData?.title || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [goalId, setGoalId] = useState(initialData?.goalId || '');
  const [dueDate, setDueDate] = useState(initialData?.dueDate || '');
  const [priority, setPriority] = useState(initialData?.priority || 'MEDIUM');
  const [estimatedMinutes, setEstimatedMinutes] = useState(initialData?.estimatedMinutes || '');
  const [dependsOnTaskId, setDependsOnTaskId] = useState(initialData?.dependsOnTaskId || '');
  const [recurring, setRecurring] = useState(initialData?.recurring || false);
  const [recurrencePattern, setRecurrencePattern] = useState(initialData?.recurrencePattern || 'DAILY');
  const [recurrenceEndDate, setRecurrenceEndDate] = useState(initialData?.recurrenceEndDate || '');

  const [goals, setGoals] = useState([]);
  const [otherTasks, setOtherTasks] = useState([]);

  useEffect(() => {
    getGoals().then(setGoals).catch(() => setGoals([]));
    getTasks()
      .then((all) => setOtherTasks(all.filter((t) => t.id !== initialData?.id)))
      .catch(() => setOtherTasks([]));
  }, [initialData?.id]);

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({
      title,
      description,
      goalId: goalId ? Number(goalId) : null,
      dueDate: dueDate || null,
      priority,
      estimatedMinutes: estimatedMinutes ? Number(estimatedMinutes) : null,
      dependsOnTaskId: dependsOnTaskId ? Number(dependsOnTaskId) : null,
      recurring,
      recurrencePattern: recurring ? recurrencePattern : 'NONE',
      recurrenceEndDate: recurring && recurrenceEndDate ? recurrenceEndDate : null,
    });
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-task-form">
      <div className="mb-3">
        <label htmlFor="taskTitle" className="form-label">Title</label>
        <input
          type="text"
          className="form-control"
          id="taskTitle"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="taskDescription" className="form-label">
          Description <span className="text-muted">(optional)</span>
        </label>
        <textarea
          className="form-control"
          id="taskDescription"
          rows="2"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <div className="row">
        <div className="col-md-6 mb-3">
          <label htmlFor="taskDueDate" className="form-label">
            Due Date <span className="text-muted">(optional)</span>
          </label>
          <input
            type="date"
            className="form-control"
            id="taskDueDate"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
          />
        </div>

        <div className="col-md-6 mb-3">
          <label htmlFor="taskPriority" className="form-label">Priority</label>
          <select
            className="form-select"
            id="taskPriority"
            value={priority}
            onChange={(e) => setPriority(e.target.value)}
          >
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="URGENT">Urgent</option>
          </select>
        </div>
      </div>

      <div className="row">
        <div className="col-md-6 mb-3">
          <label htmlFor="taskGoal" className="form-label">
            Goal <span className="text-muted">(optional)</span>
          </label>
          <select
            className="form-select"
            id="taskGoal"
            value={goalId}
            onChange={(e) => setGoalId(e.target.value)}
          >
            <option value="">None</option>
            {goals.map((g) => (
              <option key={g.id} value={g.id}>{g.title}</option>
            ))}
          </select>
        </div>

        <div className="col-md-6 mb-3">
          <label htmlFor="taskEstimate" className="form-label">
            Estimated Minutes <span className="text-muted">(optional)</span>
          </label>
          <input
            type="number"
            className="form-control"
            id="taskEstimate"
            min="1"
            value={estimatedMinutes}
            onChange={(e) => setEstimatedMinutes(e.target.value)}
          />
        </div>
      </div>

      <div className="mb-3">
        <label htmlFor="taskDependsOn" className="form-label">
          Depends On <span className="text-muted">(optional — this task can't be completed until that one is)</span>
        </label>
        <select
          className="form-select"
          id="taskDependsOn"
          value={dependsOnTaskId}
          onChange={(e) => setDependsOnTaskId(e.target.value)}
        >
          <option value="">None</option>
          {otherTasks.map((t) => (
            <option key={t.id} value={t.id}>{t.title}</option>
          ))}
        </select>
      </div>

      <div className="form-check mb-2">
        <input
          type="checkbox"
          className="form-check-input"
          id="taskRecurring"
          checked={recurring}
          onChange={(e) => setRecurring(e.target.checked)}
        />
        <label className="form-check-label" htmlFor="taskRecurring">
          Recurring task
        </label>
      </div>

      {recurring && (
        <div className="row lifesync-recurrence-fields">
          <div className="col-md-6 mb-3">
            <label htmlFor="taskRecurrencePattern" className="form-label">Repeats</label>
            <select
              className="form-select"
              id="taskRecurrencePattern"
              value={recurrencePattern}
              onChange={(e) => setRecurrencePattern(e.target.value)}
            >
              <option value="DAILY">Daily</option>
              <option value="WEEKLY">Weekly</option>
              <option value="MONTHLY">Monthly</option>
            </select>
          </div>
          <div className="col-md-6 mb-3">
            <label htmlFor="taskRecurrenceEnd" className="form-label">
              Ends on <span className="text-muted">(optional)</span>
            </label>
            <input
              type="date"
              className="form-control"
              id="taskRecurrenceEnd"
              value={recurrenceEndDate}
              onChange={(e) => setRecurrenceEndDate(e.target.value)}
            />
          </div>
        </div>
      )}

      <div className="d-flex gap-2 mt-2">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : initialData ? 'Save Changes' : 'Create Task'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default TaskForm;