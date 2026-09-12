import './PriorityBadge.css';

const PRIORITY_STYLES = {
  LOW: { label: 'Low', className: 'lifesync-priority-low' },
  MEDIUM: { label: 'Medium', className: 'lifesync-priority-medium' },
  HIGH: { label: 'High', className: 'lifesync-priority-high' },
  URGENT: { label: 'Urgent', className: 'lifesync-priority-urgent' },
};

function PriorityBadge({ priority }) {
  const style = PRIORITY_STYLES[priority] || { label: priority, className: '' };
  return <span className={`lifesync-priority-badge ${style.className}`}>{style.label}</span>;
}

export default PriorityBadge;