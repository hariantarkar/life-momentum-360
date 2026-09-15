import axiosInstance from '../../../api/axiosInstance';

export async function getEvents(from, to) {
  const params = {};
  if (from) params.from = from;
  if (to) params.to = to;
  const response = await axiosInstance.get('/calendar-events', { params });
  return response.data.data;
}

export async function getEvent(id) {
  const response = await axiosInstance.get(`/calendar-events/${id}`);
  return response.data.data;
}

export async function createEvent(payload) {
  const response = await axiosInstance.post('/calendar-events', payload);
  return response.data.data;
}

export async function updateEvent(id, payload) {
  const response = await axiosInstance.put(`/calendar-events/${id}`, payload);
  return response.data.data;
}

export async function deleteEvent(id) {
  await axiosInstance.delete(`/calendar-events/${id}`);
}