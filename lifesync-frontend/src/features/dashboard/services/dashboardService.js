import axiosInstance from '../../../api/axiosInstance';

export async function getDashboard() {
  const response = await axiosInstance.get('/dashboard');
  return response.data.data;
}