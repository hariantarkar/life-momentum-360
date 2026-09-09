import { Navigate } from 'react-router-dom';
import { useAuth } from '../features/auth/context/AuthContext';
import LoadingSpinner from '../components/common/LoadingSpinner';

/**
 * Wraps a route element and redirects to /login if there's no authenticated
 * user. Shows a spinner while we're still checking localStorage on first load,
 * so we don't flash a redirect before the session has been restored.
 */
function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return <LoadingSpinner label="Checking your session..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default ProtectedRoute;
