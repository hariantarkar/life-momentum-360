import { useState, useEffect } from 'react';
import { getEvents, createEvent, updateEvent, deleteEvent } from '../services/calendarService';
import EventItem from '../components/EventItem';
import EventForm from '../components/EventForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './CalendarPage.css';

function groupByDate(events) {
  const groups = {};
  for (const event of events) {
    const dateKey = event.startTime.slice(0, 10);
    if (!groups[dateKey]) groups[dateKey] = [];
    groups[dateKey].push(event);
  }
  Object.values(groups).forEach((list) => list.sort((a, b) => a.startTime.localeCompare(b.startTime)));
  return groups;
}

function formatDateHeading(dateKey) {
  const date = new Date(`${dateKey}T00:00:00`);
  return date.toLocaleDateString([], { weekday: 'long', month: 'long', day: 'numeric' });
}

function CalendarPage() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [conflictWarning, setConflictWarning] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingEvent, setEditingEvent] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadEvents();
  }, []);

  async function loadEvents() {
    setLoading(true);
    setError('');
    try {
      const data = await getEvents();
      setEvents(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load calendar events.');
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(payload) {
    setSubmitting(true);
    setConflictWarning('');
    try {
      await createEvent(payload);
      setShowForm(false);
      await loadEvents();
    } catch (err) {
      if (err.response?.status === 409) {
        setConflictWarning(err.response.data.message);
      } else {
        setError(err.response?.data?.message || 'Failed to create event.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  async function handleUpdate(payload) {
    setSubmitting(true);
    setConflictWarning('');
    try {
      await updateEvent(editingEvent.id, payload);
      setEditingEvent(null);
      await loadEvents();
    } catch (err) {
      if (err.response?.status === 409) {
        setConflictWarning(err.response.data.message);
      } else {
        setError(err.response?.data?.message || 'Failed to update event.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  async function handleDelete(event) {
    if (!window.confirm(`Delete "${event.title}"?`)) return;
    try {
      await deleteEvent(event.id);
      await loadEvents();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete event.');
    }
  }

  function openCreateForm() {
    setConflictWarning('');
    setShowForm(true);
  }

  function openEditForm(event) {
    setConflictWarning('');
    setEditingEvent(event);
  }

  const grouped = groupByDate(events);
  const sortedDateKeys = Object.keys(grouped).sort();

  return (
    <div className="container lifesync-calendar-page">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="lifesync-page-title">Calendar</h1>
        {!showForm && !editingEvent && (
          <button className="btn btn-primary" onClick={openCreateForm}>
            + New Event
          </button>
        )}
      </div>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {showForm && (
        <EventForm
          onSubmit={handleCreate}
          onCancel={() => { setShowForm(false); setConflictWarning(''); }}
          submitting={submitting}
          conflictWarning={conflictWarning}
        />
      )}

      {editingEvent && (
        <EventForm
          initialData={editingEvent}
          onSubmit={handleUpdate}
          onCancel={() => { setEditingEvent(null); setConflictWarning(''); }}
          submitting={submitting}
          conflictWarning={conflictWarning}
        />
      )}

      {loading ? (
        <LoadingSpinner label="Loading calendar..." />
      ) : sortedDateKeys.length === 0 ? (
        <p className="text-muted">No events scheduled. Add your first one above.</p>
      ) : (
        sortedDateKeys.map((dateKey) => (
          <div key={dateKey} className="lifesync-calendar-day-group">
            <h2 className="lifesync-calendar-day-heading">{formatDateHeading(dateKey)}</h2>
            {grouped[dateKey].map((event) => (
              <EventItem key={event.id} event={event} onEdit={openEditForm} onDelete={handleDelete} />
            ))}
          </div>
        ))
      )}
    </div>
  );
}

export default CalendarPage;