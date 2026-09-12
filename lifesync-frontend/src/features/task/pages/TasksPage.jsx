import { useState, useEffect } from 'react';
import {
  getTasks,
  getOverdueTasks,
  getDueTodayTasks,
  createTask,
  updateTask,
  markTaskComplete,
  deleteTask,
} from '../services/taskService';
import TaskItem from '../components/TaskItem';
import TaskForm from '../components/TaskForm';
import LoadingSpinner from '../../../components/common/LoadingSpinner';
import './TasksPage.css';

const FILTERS = [
  { key: 'all', label: 'All' },
  { key: 'TODO', label: 'To Do' },
  { key: 'IN_PROGRESS', label: 'In Progress' },
  { key: 'DONE', label: 'Done' },
  { key: 'overdue', label: 'Overdue' },
  { key: 'due-today', label: 'Due Today' },
];

function TasksPage() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('all');
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filter]);

  async function loadTasks() {
    setLoading(true);
    setError('');
    try {
      let data;
      if (filter === 'overdue') {
        data = await getOverdueTasks();
      } else if (filter === 'due-today') {
        data = await getDueTodayTasks();
      } else if (filter === 'all') {
        data = await getTasks();
      } else {
        data = await getTasks(filter);
      }
      setTasks(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load tasks.');
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(payload) {
    setSubmitting(true);
    try {
      await createTask(payload);
      setShowForm(false);
      await loadTasks();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create task.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleUpdate(payload) {
    setSubmitting(true);
    try {
      await updateTask(editingTask.id, payload);
      setEditingTask(null);
      await loadTasks();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update task.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleToggleComplete(task) {
    try {
      await markTaskComplete(task.id);
      await loadTasks();
    } catch (err) {
      // The backend blocks completion when a dependency isn't done yet — surface that message
      setError(err.response?.data?.message || 'Failed to complete task.');
    }
  }

  async function handleDelete(task) {
    if (!window.confirm(`Delete "${task.title}"?`)) return;
    try {
      await deleteTask(task.id);
      await loadTasks();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete task.');
    }
  }

  return (
    <div className="container lifesync-tasks-page">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h1 className="lifesync-page-title">Tasks</h1>
        {!showForm && !editingTask && (
          <button className="btn btn-primary" onClick={() => setShowForm(true)}>
            + New Task
          </button>
        )}
      </div>

      <div className="lifesync-task-filters">
        {FILTERS.map((f) => (
          <button
            key={f.key}
            className={`btn btn-sm ${filter === f.key ? 'btn-primary' : 'btn-outline-secondary'}`}
            onClick={() => setFilter(f.key)}
          >
            {f.label}
          </button>
        ))}
      </div>

      {error && (
        <div className="alert alert-danger mt-3" role="alert">
          {error}
        </div>
      )}

      {showForm && (
        <TaskForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} submitting={submitting} />
      )}

      {editingTask && (
        <TaskForm
          initialData={editingTask}
          onSubmit={handleUpdate}
          onCancel={() => setEditingTask(null)}
          submitting={submitting}
        />
      )}

      <div className="mt-3">
        {loading ? (
          <LoadingSpinner label="Loading tasks..." />
        ) : tasks.length === 0 ? (
          <p className="text-muted">No tasks here.</p>
        ) : (
          tasks.map((task) => (
            <TaskItem
              key={task.id}
              task={task}
              onToggleComplete={handleToggleComplete}
              onEdit={setEditingTask}
              onDelete={handleDelete}
            />
          ))
        )}
      </div>
    </div>
  );
}

export default TasksPage;