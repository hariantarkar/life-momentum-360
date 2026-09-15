import axiosInstance from '../../../api/axiosInstance';

export async function getHabits() {
  const response = await axiosInstance.get('/habits');
  return response.data.data;
}

export async function getHabit(id) {
  const response = await axiosInstance.get(`/habits/${id}`);
  return response.data.data;
}

export async function createHabit(payload) {
  const response = await axiosInstance.post('/habits', payload);
  return response.data.data;
}

export async function updateHabit(id, payload) {
  const response = await axiosInstance.put(`/habits/${id}`, payload);
  return response.data.data;
}

export async function deleteHabit(id) {
  await axiosInstance.delete(`/habits/${id}`);
}

export async function logHabitToday(id) {
  const response = await axiosInstance.patch(`/habits/${id}/log`);
  return response.data.data;
}

export async function unlogHabitToday(id) {
  const response = await axiosInstance.delete(`/habits/${id}/log`);
  return response.data.data;
}

export async function getHabitLogs(id) {
  const response = await axiosInstance.get(`/habits/${id}/logs`);
  return response.data.data;
}