import './LoadingSpinner.css';

function LoadingSpinner({ label = 'Loading...' }) {
  return (
    <div className="lifesync-spinner-wrapper">
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">{label}</span>
      </div>
      <p className="lifesync-spinner-label">{label}</p>
    </div>
  );
}

export default LoadingSpinner;
