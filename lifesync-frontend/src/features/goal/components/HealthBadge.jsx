import './HealthBadge.css';

const HEALTH_STYLES = {
  ON_TRACK: { label: 'On Track', className: 'lifesync-badge-on-track' },
  AT_RISK: { label: 'At Risk', className: 'lifesync-badge-at-risk' },
  OVERDUE: { label: 'Overdue', className: 'lifesync-badge-overdue' },
  COMPLETED: { label: 'Completed', className: 'lifesync-badge-completed' },
};

function HealthBadge({ health }) {
  const style = HEALTH_STYLES[health] || { label: health, className: '' };
  return <span className={`lifesync-health-badge ${style.className}`}>{style.label}</span>;
}

export default HealthBadge;