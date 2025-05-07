import { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

const Signup = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    setFormData({ 
      ...formData, 
      [e.target.name]: e.target.value 
    });
  };

  const handleSignup = async () => {
    try {
      const checkResponse = await axios.get(`http://localhost:8080/api/users/email/${formData.email}`);
  
      if (checkResponse.data) {
        alert('Email already in use. Please choose another one.');
      } else {
        const response = await axios.post('http://localhost:8080/api/users', formData);
  
        if (response.status === 201) {
          const user = await axios.get(`http://localhost:8080/api/users/${formData.email}`);
          localStorage.setItem('userId', user.id);
          navigate('/home');
        } else {
          alert('Signup failed');
        }
      }
    } catch (error) {
      console.error(error);
      if (error.response) {
        alert(error.response.data.message || 'Signup error');
      } else {
        alert('Signup error');
      }
    }
  };

  return (
    <div className="w-full min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-white px-4">
      <div className="w-full max-w-md mx-auto p-8 rounded-2xl shadow-2xl bg-gray-800 border border-gray-700">
        <h2 className="text-3xl font-bold text-white mb-6 text-center">Sign Up</h2>
        <form className="space-y-5">
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-300">Username</label>
            <input
              type="text"
              name="username"
              id="username"
              value={formData.username}
              onChange={handleChange}
              className="mt-1 w-full px-4 py-3 bg-gray-700 border border-gray-600 rounded-xl placeholder-gray-400 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="Your username"
            />
          </div>
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-300">Email address</label>
            <input
              type="email"
              name="email"
              id="email"
              value={formData.email}
              onChange={handleChange}
              className="mt-1 w-full px-4 py-3 bg-gray-700 border border-gray-600 rounded-xl placeholder-gray-400 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="you@example.com"
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-300">Password</label>
            <input
              type="password"
              name="password"
              id="password"
              value={formData.password}
              onChange={handleChange}
              className="mt-1 w-full px-4 py-3 bg-gray-700 border border-gray-600 rounded-xl placeholder-gray-400 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="••••••••"
            />
          </div>
          <div>
            <button
              type="button"
              onClick={handleSignup}
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 rounded-xl shadow-md transition duration-300"
            >
              Sign Up
            </button>
          </div>
        </form>
        <p className="mt-6 text-center text-sm text-gray-400">
          Already have an account?{' '}
          <Link to="/login" className="text-indigo-400 hover:underline font-medium">
            Login here
          </Link>
        </p>
      </div>
    </div>
  );
  
};

export default Signup;
