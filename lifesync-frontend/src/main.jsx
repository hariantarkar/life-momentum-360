import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

// Bootstrap's compiled CSS — gives us the grid, utility classes, form styles,
// buttons, alerts, spinners, etc. used throughout the app.
import 'bootstrap/dist/css/bootstrap.min.css';

// Our own global overrides/base styles, loaded AFTER Bootstrap so our rules
// can override Bootstrap's defaults where needed.
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
