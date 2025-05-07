import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Home from './components/Home';
import Sidebar from './components/Sidebar';
import TodoTab from './components/TodoTab';
import KanbanTab from './components/KanbanTab';
import UrgencyMatrixTab from './components/UrgencyMatrixTab';
import TimeBlockTab from './components/TimeBlockTab';
import Login from './components/Login';
import Signup from './components/SignUp';

function App() {

  const isLoggedIn = !!localStorage.getItem('userEmail');

  return (
    <Router>
      <div className="flex h-screen bg-gray-100">
        <div className="flex-1 overflow-auto">
          <Routes>
            <Route path="/" element={isLoggedIn ? <Navigate to="/home" /> : <Navigate to="/login" />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/home" element={<Home />} />
            <Route path="/todos" element={<TodoTab />} />
            <Route path="/kanban" element={<KanbanTab />} />
            <Route path="/urgency-matrix" element={<UrgencyMatrixTab />} />
            <Route path="/time-block" element={<TimeBlockTab />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
