import { useState, useEffect } from 'react';
import {
  getLifeAreas,
  createLifeArea,
  updateLifeArea,
  deleteLifeArea,
} from '../services/lifeAreaService';
import LifeAreaCard from '../components/LifeAreaCard';
import LifeAreaForm from '../components/LifeAreaForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './LifeAreasPage.css';

function LifeAreasPage() {
  const [lifeAreas, setLifeAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const [formMode, setFormMode] = useState(null); // null | 'create' | { editing life area object }

  useEffect(() => {
    loadLifeAreas();
  }, []);

  async function loadLifeAreas() {
    setLoading(true);
    setError('');
    try {
      const data = await getLifeAreas();
      setLifeAreas(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load life areas.');
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(payload) {
    setSubmitting(true);
    try {
      await createLifeArea(payload);
      setFormMode(null);
      await loadLifeAreas();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create life area.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleUpdate(payload) {
    setSubmitting(true);
    try {
      await updateLifeArea(formMode.id, payload);
      setFormMode(null);
      await loadLifeAreas();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update life area.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleDelete(lifeArea) {
    if (!window.confirm(`Delete "${lifeArea.name}"? This can't be undone.`)) {
      return;
    }
    try {
      await deleteLifeArea(lifeArea.id);
      await loadLifeAreas();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete life area.');
    }
  }

  const isEditing = formMode && formMode !== 'create';

  return (
    <div className="container lifesync-lifeareas-page">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="lifesync-page-title">Life Areas</h1>
        {!formMode && (
          <button className="btn btn-primary" onClick={() => setFormMode('create')}>
            + Add Life Area
          </button>
        )}
      </div>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {formMode === 'create' && (
        <LifeAreaForm onSubmit={handleCreate} onCancel={() => setFormMode(null)} submitting={submitting} />
      )}

      {isEditing && (
        <LifeAreaForm
          initialData={formMode}
          onSubmit={handleUpdate}
          onCancel={() => setFormMode(null)}
          submitting={submitting}
        />
      )}

      {loading ? (
        <LoadingSpinner label="Loading life areas..." />
      ) : lifeAreas.length === 0 ? (
        <p className="text-muted">
          No life areas yet. Create your first one — Career, Health, Finance, Learning, whatever
          matters to you.
        </p>
      ) : (
        lifeAreas.map((la) => (
          <LifeAreaCard
            key={la.id}
            lifeArea={la}
            onEdit={(area) => setFormMode(area)}
            onDelete={handleDelete}
          />
        ))
      )}
    </div>
  );
}

export default LifeAreasPage;
