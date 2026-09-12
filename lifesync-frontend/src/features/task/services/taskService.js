import axiosInstance from '../../../api/axiosInstance';

export async function getTasks(status) {
  const response = await axiosInstance.get('/tasks', { params: status ? { status } : {} });
  return response.data.data;
}

export async function getOverdueTasks() {
  const response = await axiosInstance.get('/tasks/overdue');
  return response.data.data;
}

export async function getDueTodayTasks() {
  const response = await axiosInstance.get('/tasks/due-today');
  return response.data.data;
}

export async function getTask(id) {
  const response = await axiosInstance.get(`/tasks/${id}`);
  return response.data.data;
}

export async function createTask(payload) {
  const response = await axiosInstance.post('/tasks', payload);
  return response.data.data;
}

export async function updateTask(id, payload) {
  const response = await axiosInstance.put(`/tasks/${id}`, payload);
  return response.data.data;
}

export async function markTaskComplete(id) {
  const response = await axiosInstance.patch(`/tasks/${id}/complete`);
  return response.data.data;
}

export async function deleteTask(id) {
  await axiosInstance.delete(`/tasks/${id}`);
}