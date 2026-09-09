import axiosInstance from '../../../api/axiosInstance';

export async function registerUser(payload) {
  // payload: { fullName, email, password, phone }
  const response = await axiosInstance.post('/auth/register', payload);
  return response.data.data;
}

export async function loginUser(payload) {
  // payload: { email, password }
  const response = await axiosInstance.post('/auth/login', payload);
  return response.data.data; // { accessToken, refreshToken, tokenType, expiresInMs, user }
}

export async function logoutUser(refreshToken) {
  const response = await axiosInstance.post('/auth/logout', { refreshToken });
  return response.data;
}

export async function fetchCurrentUser() {
  const response = await axiosInstance.get('/auth/me');
  return response.data.data;
}
