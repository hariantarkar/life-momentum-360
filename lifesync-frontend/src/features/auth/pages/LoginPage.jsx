import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './AuthPages.css';

function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    setFormError('');
    setSubmitting(true);

    const result = await login(email, password);

    setSubmitting(false);

    if (result.success) {
      navigate('/dashboard');
    } else {
      setFormError(result.message);
    }
  }

  return (
    <div className="lifesync-auth-page">
      <div className="lifesync-auth-card">
        <h2 className="lifesync-auth-title">Welcome back</h2>
        <p className="lifesync-auth-subtitle">Log in to keep your momentum going.</p>

        {formError && (
          <div className="alert alert-danger" role="alert">
            {formError}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoComplete="email"
            />
          </div>

          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="current-password"
            />
          </div>

          <button type="submit" className="btn btn-primary w-100" disabled={submitting}>
            {submitting ? 'Logging in...' : 'Log In'}
          </button>
        </form>

        <p className="lifesync-auth-footer">
          Don't have an account? <Link to="/register">Sign up</Link>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;
