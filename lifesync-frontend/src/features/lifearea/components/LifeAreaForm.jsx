import { useState } from 'react';
import './LifeAreaForm.css';

const PRESET_COLORS = ['#4f46e5', '#059669', '#d97706', '#dc2626', '#7c3aed', '#0891b2'];

function LifeAreaForm({ initialData, onSubmit, onCancel, submitting }) {
  const [name, setName] = useState(initialData?.name || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [colorCode, setColorCode] = useState(initialData?.colorCode || PRESET_COLORS[0]);
  const [icon, setIcon] = useState(initialData?.icon || '');

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({ name, description, colorCode, icon });
  }

  return (
    <form onSubmit={handleSubmit} className="lifesync-lifearea-form">
      <div className="mb-3">
        <label htmlFor="laName" className="form-label">Name</label>
        <input
          type="text"
          className="form-control"
          id="laName"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="e.g. Career, Finance, Health"
          required
        />
      </div>

      <div className="mb-3">
        <label htmlFor="laDescription" className="form-label">
          Description <span className="text-muted">(optional)</span>
        </label>
        <textarea
          className="form-control"
          id="laDescription"
          rows="2"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <div className="mb-3">
        <label className="form-label d-block">Color</label>
        <div className="lifesync-color-swatches">
          {PRESET_COLORS.map((color) => (
            <button
              type="button"
              key={color}
              className={`lifesync-color-swatch ${colorCode === color ? 'lifesync-color-swatch-selected' : ''}`}
              style={{ backgroundColor: color }}
              onClick={() => setColorCode(color)}
              aria-label={`Select color ${color}`}
            />
          ))}
        </div>
      </div>

      <div className="mb-3">
        <label htmlFor="laIcon" className="form-label">
          Icon key <span className="text-muted">(optional, for future frontend use)</span>
        </label>
        <input
          type="text"
          className="form-control"
          id="laIcon"
          value={icon}
          onChange={(e) => setIcon(e.target.value)}
          placeholder="e.g. briefcase, heart, book"
        />
      </div>

      <div className="d-flex gap-2">
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : initialData ? 'Save Changes' : 'Create Life Area'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}

export default LifeAreaForm;

