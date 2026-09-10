import axiosInstance from '../../../api/axiosInstance';

export async function getLifeAreas() {
  const response = await axiosInstance.get('/life-areas');
  return response.data.data;
}

export async function getLifeArea(id) {
  const response = await axiosInstance.get(`/life-areas/${id}`);
  return response.data.data;
}

export async function createLifeArea(payload) {
  const response = await axiosInstance.post('/life-areas', payload);
  return response.data.data;
}

export async function updateLifeArea(id, payload) {
  const response = await axiosInstance.put(`/life-areas/${id}`, payload);
  return response.data.data;
}

export async function deleteLifeArea(id) {
  await axiosInstance.delete(`/life-areas/${id}`);
}