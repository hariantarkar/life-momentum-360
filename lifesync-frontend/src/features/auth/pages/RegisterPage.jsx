import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './AuthPages.css';

function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    setFormError('');
    setSuccessMessage('');
    setSubmitting(true);

    const result = await register(fullName, email, password, phone);

    setSubmitting(false);

    if (result.success) {
      setSuccessMessage('Account created! Redirecting to login...');
      setTimeout(() => navigate('/login'), 1500);
    } else {
      setFormError(result.message);
    }
  }

  return (
    <div className="lifesync-auth-page">
      <div className="lifesync-auth-card">
        <h2 className="lifesync-auth-title">Create your account</h2>
        <p className="lifesync-auth-subtitle">Start connecting your goals, habits, and life.</p>

        {formError && (
          <div className="alert alert-danger" role="alert">
            {formError}
          </div>
        )}

        {successMessage && (
          <div className="alert alert-success" role="alert">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="fullName" className="form-label">
              Full Name
            </label>
            <input
              type="text"
              className="form-control"
              id="fullName"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
              autoComplete="name"
            />
          </div>

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
              minLength={8}
              autoComplete="new-password"
            />
            <div className="form-text">At least 8 characters.</div>
          </div>

          <div className="mb-3">
            <label htmlFor="phone" className="form-label">
              Phone <span className="text-muted">(optional)</span>
            </label>
            <input
              type="tel"
              className="form-control"
              id="phone"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              autoComplete="tel"
            />
          </div>

          <button type="submit" className="btn btn-primary w-100" disabled={submitting}>
            {submitting ? 'Creating account...' : 'Sign Up'}
          </button>
        </form>

        <p className="lifesync-auth-footer">
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </div>
    </div>
  );
}

export default RegisterPage;
