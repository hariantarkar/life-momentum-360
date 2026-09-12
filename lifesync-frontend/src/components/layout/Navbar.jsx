import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../features/auth/context/AuthContext';
import './Navbar.css';

function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate('/login');
  }

  return (
    <nav className="navbar navbar-expand-lg lifesync-navbar">
      <div className="container">
        <Link className="navbar-brand lifesync-brand" to="/">
          LifeSync
        </Link>

        {isAuthenticated && (
          <div className="lifesync-navbar-links">
            <Link to="/dashboard" className="lifesync-nav-link">
              Dashboard
            </Link>
            <Link to="/life-areas" className="lifesync-nav-link">
              Life Areas
            </Link>
            <Link to="/goals" className="lifesync-nav-link">
              Goals
            </Link>
              <Link to="/tasks" className="lifesync-nav-link">
              Tasks
            </Link>
          </div>
        )}

        <div className="d-flex align-items-center ms-auto">
          {isAuthenticated ? (
            <>
              <span className="lifesync-navbar-username me-3">
                Hi, {user?.fullName?.split(' ')[0]}
              </span>
              <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-outline-light btn-sm me-2">
                Login
              </Link>
              <Link to="/register" className="btn btn-light btn-sm">
                Sign Up
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;