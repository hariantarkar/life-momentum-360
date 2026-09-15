import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../features/auth/pages/LoginPage';
import RegisterPage from '../features/auth/pages/RegisterPage';
import DashboardPage from '../features/dashboard/pages/DashboardPage';
import LifeAreasPage from '../features/lifearea/pages/LifeAreasPage';
import GoalsPage from '../features/goal/pages/GoalsPage';
import GoalDetailPage from '../features/goal/pages/GoalDetailPage';
import TasksPage from '../features/task/pages/TasksPage';
import HabitsPage from '../features/habit/pages/HabitsPage';
import ProtectedRoute from './ProtectedRoute';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/life-areas"
        element={
          <ProtectedRoute>
            <LifeAreasPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/goals"
        element={
          <ProtectedRoute>
            <GoalsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/goals/:id"
        element={
          <ProtectedRoute>
            <GoalDetailPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/tasks"
        element={
          <ProtectedRoute>
            <TasksPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/habits"
        element={
          <ProtectedRoute>
            <HabitsPage />
          </ProtectedRoute>
        }
      />

      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default AppRoutes;