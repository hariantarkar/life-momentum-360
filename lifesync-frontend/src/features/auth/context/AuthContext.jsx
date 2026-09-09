import { createContext, useContext, useState, useEffect } from 'react';
import { loginUser, registerUser, logoutUser } from '../services/authService';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // true while we check localStorage on first load
  const [error, setError] = useState(null);

  // On first mount, restore the session from localStorage if a user was already logged in
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const storedAccessToken = localStorage.getItem('accessToken');

    if (storedUser && storedAccessToken) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  async function login(email, password) {
    setError(null);
    try {
      const data = await loginUser({ email, password });
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('refreshToken', data.refreshToken);
      localStorage.setItem('user', JSON.stringify(data.user));
      setUser(data.user);
      return { success: true };
    } catch (err) {
      const message = err.response?.data?.message || 'Login failed. Please try again.';
      setError(message);
      return { success: false, message };
    }
  }

  async function register(fullName, email, password, phone) {
    setError(null);
    try {
      await registerUser({ fullName, email, password, phone });
      return { success: true };
    } catch (err) {
      const message = err.response?.data?.message || 'Registration failed. Please try again.';
      setError(message);
      return { success: false, message };
    }
  }

  async function logout() {
    const refreshToken = localStorage.getItem('refreshToken');
    try {
      if (refreshToken) {
        await logoutUser(refreshToken);
      }
    } catch (err) {
      // Even if the server call fails, we still clear local session state below —
      // the user should always be able to log out client-side.
      console.error('Logout request failed:', err);
    } finally {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      setUser(null);
    }
  }

  const value = {
    user,
    loading,
    error,
    isAuthenticated: !!user,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
