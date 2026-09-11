import axiosInstance from '../../../api/axiosInstance';

export async function getGoals() {
  const response = await axiosInstance.get('/goals');
  return response.data.data;
}

export async function getGoal(id) {
  const response = await axiosInstance.get(`/goals/${id}`);
  return response.data.data;
}

export async function createGoal(payload) {
  const response = await axiosInstance.post('/goals', payload);
  return response.data.data;
}

export async function updateGoal(id, payload) {
  const response = await axiosInstance.put(`/goals/${id}`, payload);
  return response.data.data;
}

export async function markGoalComplete(id) {
  const response = await axiosInstance.patch(`/goals/${id}/complete`);
  return response.data.data;
}

export async function deleteGoal(id) {
  await axiosInstance.delete(`/goals/${id}`);
}