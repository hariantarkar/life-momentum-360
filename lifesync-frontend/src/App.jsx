import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './features/auth/context/AuthContext';
import Navbar from './components/layout/Navbar';
import AppRoutes from './routes/AppRoutes';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Navbar />
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
