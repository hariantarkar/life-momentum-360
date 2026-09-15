import './EventTypeBadge.css';

const TYPE_STYLES = {
  EVENT: { label: 'Event', className: 'lifesync-eventtype-event' },
  FOCUS_BLOCK: { label: 'Focus Block', className: 'lifesync-eventtype-focus' },
  DEADLINE: { label: 'Deadline', className: 'lifesync-eventtype-deadline' },
};

function EventTypeBadge({ eventType }) {
  const style = TYPE_STYLES[eventType] || { label: eventType, className: '' };
  return <span className={`lifesync-eventtype-badge ${style.className}`}>{style.label}</span>;
}

export default EventTypeBadge;