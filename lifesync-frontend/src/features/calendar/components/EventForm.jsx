import { useState, useEffect } from 'react';
import { getGoals } from '../../goal/services/goalService';
import { getTasks } from '../../task/services/taskService';
import './EventForm.css';

function EventForm({ initialData, onSubmit, onCancel, submitting, conflictWarning }) {
  const [title, setTitle] = useState(initialData?.title || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [eventType, setEventType] = useState(initialData?.eventType || 'EVENT');
  const [startTime, setStartTime] = useState(initialData?.startTime?.slice(0, 16) || '');
  const [endTime, setEndTime] = useState(initialData?.endTime?.slice(0, 16) || '');
  const [location, setLocation] = useState(initialData?.location || '');
  const [goalId, setGoalId] = useState(initialData?.goalId || '');
  const [taskId, setTaskId] = useState(initialData?.taskId || '');
  const [allowOverlap, setAllowOverlap] = useState(false);

  const [goals, setGoals] = useState([]);
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    getGoals().then(setGoals).catch(() => setGoals([]));
    getTasks().then(setTasks).catch(() => setTasks([]));
  }, []);

  useEffect(() => {
    if (conflictWarning) setAllowOverlap(true);
  }, [conflictWarning]);

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({
      title,
      description,
      eventType,
      startTime,
      endTime,
      location,
      goalId: goalId ? Number(goalId) : null,
      taskId: taskId ? Number(taskId) : null,
      allowOverlap,
    });
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-event-form">
      {conflictWarning && (
        <div className="alert alert-warning" role="alert">
          {conflictWarning} The "Allow overlap" box below has been checked for you — submit again
          if you really want this time slot.
        </div>
      )}

      <div className="mb-3">
        <label htmlFor="eventTitle" className="form-label">Title</label>
        <input
          type="text"
          className="form-control"
          id="eventTitle"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="eventDescription" className="form-label">
          Description <span className="text-muted">(optional)</span>
        </label>
        <textarea
          className="form-control"
          id="eventDescription"
          rows="2"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <div className="row">
        <div className="col-md-4 mb-3">
          <label htmlFor="eventType" className="form-label">Type</label>
          <select
            className="form-select"
            id="eventType"
            value={eventType}
            onChange={(e) => setEventType(e.target.value)}
          >
            <option value="EVENT">Event</option>
            <option value="FOCUS_BLOCK">Focus Block</option>
            <option value="DEADLINE">Deadline</option>
          </select>
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="eventStart" className="form-label">Start</label>
          <input
            type="datetime-local"
            className="form-control"
            id="eventStart"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
          />
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="eventEnd" className="form-label">End</label>
          <input
            type="datetime-local"
            className="form-control"
            id="eventEnd"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            required
          />
        </div>
      </div>

      <div className="mb-3">
        <label htmlFor="eventLocation" className="form-label">
          Location <span className="text-muted">(optional)</span>
        </label>
        <input
          type="text"
          className="form-control"
          id="eventLocation"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
      </div>

      <div className="row">
        <div className="col-md-6 mb-3">
          <label htmlFor="eventGoal" className="form-label">
            Link to Goal <span className="text-muted">(optional)</span>
          </label>
          <select
            className="form-select"
            id="eventGoal"
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
          <label htmlFor="eventTask" className="form-label">
            Link to Task <span className="text-muted">(optional)</span>
          </label>
          <select
            className="form-select"
            id="eventTask"
            value={taskId}
            onChange={(e) => setTaskId(e.target.value)}
          >
            <option value="">None</option>
            {tasks.map((t) => (
              <option key={t.id} value={t.id}>{t.title}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="form-check mb-3">
        <input
          type="checkbox"
          className="form-check-input"
          id="eventAllowOverlap"
          checked={allowOverlap}
          onChange={(e) => setAllowOverlap(e.target.checked)}
        />
        <label className="form-check-label" htmlFor="eventAllowOverlap">
          Allow overlap with existing events
        </label>
      </div>

      <div className="d-flex gap-2">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : initialData ? 'Save Changes' : 'Create Event'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default EventForm;