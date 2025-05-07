import { Link } from 'react-router-dom';
import Sidebar from './Sidebar';

const Home = () => {

  return (
  <div className="flex h-screen bg-gray-950 text-gray-100">
    <Sidebar />

    <div className="flex-1 flex flex-col">
      <header className="lg:hidden p-4 bg-gray-900 text-white flex items-center justify-between shadow-md">
        <div className="font-semibold text-lg">Dashboard</div>
      </header>

      <div className="flex-1 p-8 bg-gray-950">
        <h1 className="text-4xl font-semibold text-white mb-4">Welcome back,  👋</h1>
        <p className="text-lg text-gray-400 mb-8">
          Your minimal, focused workspace.
        </p>

        <div className="bg-gray-900 p-6 rounded-2xl shadow-inner border border-gray-800">
          <h2 className="text-2xl font-medium mb-2 text-indigo-400">Today's Focus</h2>
          <p className="text-gray-400">Start by organizing your thoughts, tasks or notes here...</p>
        </div>

      </div>
    </div>
  </div>
);

};

export default Home;
