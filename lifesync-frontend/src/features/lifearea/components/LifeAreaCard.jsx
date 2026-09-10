import './LifeAreaCard.css';

function LifeAreaCard({ lifeArea, onEdit, onDelete }) {
  return (
    <div className="lifesync-lifearea-card" style={{ borderLeftColor: lifeArea.colorCode || '#9ca3af' }}>
      <div className="lifesync-lifearea-card-body">
        <h5 className="lifesync-lifearea-card-title">{lifeArea.name}</h5>
        {lifeArea.description && (
          <p className="lifesync-lifearea-card-description">{lifeArea.description}</p>
        )}
      </div>
      <div className="lifesync-lifearea-card-actions">
        <button className="btn btn-sm btn-outline-secondary" onClick={() => onEdit(lifeArea)}>
          Edit
        </button>
        <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(lifeArea)}>
          Delete
        </button>
      </div>
    </div>
  );
}

export default LifeAreaCard;