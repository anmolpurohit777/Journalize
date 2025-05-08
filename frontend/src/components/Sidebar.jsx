import { NavLink } from 'react-router-dom';

const Sidebar = () => {
  const navItems = [
    { name: 'Home', path: '/home' },
    { name: 'Journal', path: '/journal' },
    { name: 'Todos', path: '/todos' },
    { name: 'Kanban', path: '/kanban' },
    { name: 'Urgency Matrix', path: '/urgency-matrix' },
    { name: 'Time Block', path: '/time-block' },
  ];

  const handleLogout = () => {
    localStorage.removeItem('userEmail');
    window.location.href = '/login';
  };

  return (
    <div className="w-64 h-screen bg-[#1e1e2e] text-white flex flex-col justify-between p-6 border-r border-gray-800 shadow-lg">
      <div>
        <h2 className="text-3xl font-extrabold mb-10 tracking-wide text-blue-400 font-sans">JOURNALIZE</h2>
        <nav className="flex flex-col space-y-3">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `text-base font-medium px-3 py-2 rounded-lg transition-all duration-200 ease-in-out ${
                  isActive
                    ? 'bg-gray-800 text-blue-400 shadow-md'
                    : 'text-gray-300 hover:bg-gray-700 hover:text-white'
                }`
              }
            >
              {item.name}
            </NavLink>
          ))}
        </nav>
      </div>
  
      <button
        onClick={handleLogout}
        className="mt-10 w-full py-2 px-4 bg-red-600 hover:bg-red-700 text-white font-semibold rounded-lg shadow-sm transition duration-300 ease-in-out"
      >
        Logout
      </button>
    </div>
  );
  
  
  
};

export default Sidebar;
