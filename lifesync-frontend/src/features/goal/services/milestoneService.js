import axiosInstance from '../../../api/axiosInstance';

export async function getMilestones(goalId) {
  const response = await axiosInstance.get(`/goals/${goalId}/milestones`);
  return response.data.data;
}

export async function createMilestone(goalId, payload) {
  const response = await axiosInstance.post(`/goals/${goalId}/milestones`, payload);
  return response.data.data;
}

export async function updateMilestone(goalId, milestoneId, payload) {
  const response = await axiosInstance.put(`/goals/${goalId}/milestones/${milestoneId}`, payload);
  return response.data.data;
}

export async function toggleMilestoneComplete(goalId, milestoneId) {
  const response = await axiosInstance.patch(`/goals/${goalId}/milestones/${milestoneId}/toggle-complete`);
  return response.data.data;
}

export async function deleteMilestone(goalId, milestoneId) {
  await axiosInstance.delete(`/goals/${goalId}/milestones/${milestoneId}`);
}