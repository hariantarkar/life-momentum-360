import EventTypeBadge from './EventTypeBadge';
import './EventItem.css';

function formatTime(isoString) {
  const d = new Date(isoString);
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function EventItem({ event, onEdit, onDelete }) {
  return (
    <div className="lifesync-event-item">
      <div className="lifesync-event-item-time">
        {formatTime(event.startTime)} – {formatTime(event.endTime)}
      </div>

      <div className="lifesync-event-item-body">
        <div className="d-flex align-items-center gap-2 flex-wrap">
          <span className="lifesync-event-item-title">{event.title}</span>
          <EventTypeBadge eventType={event.eventType} />
        </div>
        <div className="lifesync-event-item-meta">
          {event.location && <span>📍 {event.location}</span>}
          {event.goalTitle && <span>Goal: {event.goalTitle}</span>}
          {event.taskTitle && <span>Task: {event.taskTitle}</span>}
        </div>
      </div>

      <div className="lifesync-event-item-actions">
        <button className="btn btn-sm btn-outline-secondary" onClick={() => onEdit(event)}>
          Edit
        </button>
        <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(event)}>
          Delete
        </button>
      </div>
    </div>
  );
}

export default EventItem;