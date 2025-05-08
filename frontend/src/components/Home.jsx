import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Sidebar from './Sidebar';
import axios from 'axios';
import dayjs from 'dayjs';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const userId = localStorage.getItem("userId");
const MAX_GOALS = 3;
const COLORS = ['#3b82f6', '#22c55e', '#f59e0b', '#ef4444', '#a855f7', '#14b8a6', '#6b7280'];
const quotes = [
    "The only way to do great work is to love what you do. — Steve Jobs",
    "Success is not the key to happiness. Happiness is the key to success. — Albert Schweitzer",
    "Don’t watch the clock; do what it does. Keep going. — Sam Levenson",
    "Opportunities don't happen, you create them. — Chris Grosser",
    "Success usually comes to those who are too busy to be looking for it. — Henry David Thoreau",
    "The harder you work for something, the greater you'll feel when you achieve it. — Anonymous",
    "Dream it. Wish it. Do it. — Anonymous",
    "It always seems impossible until it's done. — Nelson Mandela",
    "The future depends on what you do today. — Mahatma Gandhi",
    "The way to get started is to quit talking and begin doing. — Walt Disney",
];

const Home = () => {
  const [goals, setGoals] = useState([]);
  const [timeblocks, setTimeblocks] = useState([]);
  const [randomQuote, setRandomQuote] = useState("");
  const [showGoalForm, setShowGoalForm] = useState(false);
  const [goalName, setGoalName] = useState(''); 
  const [goalDate, setGoalDate] = useState('');

  const [userName, setUserName] = useState('');

  useEffect(() => {
    const fetchUserName = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/users/${userId}`);
        setUserName(response.data.username);
      } catch (error) {
        console.error('Error fetching user name:', error);
      }
    };

    fetchUserName();
  }, []);

  useEffect(() => {
    const fetchQuote = async () => {
      const randomIndex = Math.floor(Math.random() * quotes.length);
      setRandomQuote(quotes[randomIndex]);
    };

    fetchQuote();
  }, []);

  useEffect(() => {
    fetchGoals();
  }, []);

  const fetchGoals = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/users/${userId}/countdown-goals`);
      setGoals(response.data);
    } catch (error) {
      console.error('Error fetching goals:', error);
    }
  };

  useEffect(() => {
    const fetchTimeblocks = async () => {
      const res = await fetch(`http://localhost:8080/api/users/${userId}/timeblocks`);
      const data = await res.json();
      setTimeblocks(data);
    };

    fetchTimeblocks();
  }, []);

  const handleAddGoal = async (e) => {
    e.preventDefault();

    if (!goalName || !dayjs(goalDate).isValid()) {
      alert('Invalid input.');
      return;
    }

    try {
      await axios.post(`http://localhost:8080/api/users/${userId}/countdown-goals`, {
        goal: goalName,
        endDate: dayjs(goalDate).format('YYYY-MM-DD')
      });
      fetchGoals();
      setShowGoalForm(false);
      setGoalName('');
      setGoalDate('');
    } catch (error) {
      console.error('Add goal failed', error);
    }
  };

  const handleDelete = async (goalId) => {
    try {
      await axios.delete(`http://localhost:8080/api/users/${userId}/countdown-goals/${goalId}`);
      fetchGoals();
    } catch (error) {
      console.error('Delete failed', error);
    }
  };

  const parseTimeToHours = (time) => {
    const [hours, minutes] = time.split(':').map(Number);
    return hours + minutes / 60;
  };

  const getTimeBlockChartData = (timeblocks) => {
    let totalUsed = 0;
    const taskData = timeblocks.map((block) => {
      const start = parseTimeToHours(block.startTime);
      const end = parseTimeToHours(block.endTime);
      const hours = Math.max(end - start, 0);
      totalUsed += hours;
      return {
        name: block.description,
        value: parseFloat(hours.toFixed(2)),
      };
    });

    if (totalUsed < 24) {
      taskData.push({
        name: 'Others',
        value: parseFloat((24 - totalUsed).toFixed(2)),
      });
    }

    return taskData;
  };

  return (
    <div className="flex h-screen bg-gray-950 text-gray-100">
      <Sidebar />

      <div className="flex-1 flex flex-col">
        <header className="lg:hidden p-4 bg-gray-900 text-white flex items-center justify-between shadow-md">
          <div className="font-semibold text-lg">Dashboard</div>
        </header>

        <div className="flex-1 p-8 bg-gray-950 overflow-auto">
        <h1 className="text-4xl font-semibold text-blue-400 mb-4 text-center">Welcome back {userName}</h1>
          <p className="text-lg text-gray-400 mb-8 text-center">Your minimal, focused workspace.</p>

          <div className="bg-gray-900 p-6 rounded-2xl shadow-inner border border-gray-800 mb-8">
            <h2 className="text-2xl font-medium mb-2 text-indigo-400">Today's Motivation</h2>
            <p className="text-gray-400 italic">“{randomQuote}”</p>
          </div>

          <div className="flex gap-6">
            {[...Array(MAX_GOALS)].map((_, i) =>
              goals[i] ? (
                <div
                  key={goals[i].id}
                  className="relative bg-gray-900 text-white p-4 rounded-2xl shadow-md flex-1 flex flex-col items-center"
                >
                  <button
                    onClick={() => handleDelete(goals[i].id)}
                    className="absolute top-2 right-3 text-gray-400 hover:text-red-500 text-xl"
                    title="Delete goal"
                  >
                    ×
                  </button>

                  <div className="relative w-36 h-36 mb-2">
                    <svg viewBox="0 0 36 36" className="w-full h-full transform -rotate-90">
                      {[...Array(30)].map((_, segIndex) => {
                        const daysLeft = dayjs(goals[i].endDate).diff(dayjs(), 'day');
                        const filled = Math.max(0, Math.min(30, Math.floor((1 - daysLeft / 60) * 30)));
                        return (
                          <circle
                            key={segIndex}
                            cx="18"
                            cy="18"
                            r="16"
                            fill="transparent"
                            stroke={segIndex < filled ? '#3b82f6' : '#4b5563'}
                            strokeWidth="3"
                            strokeDasharray="1 5"
                            transform={`rotate(${(360 / 30) * segIndex}, 18, 18)`}
                          />
                        );
                      })}
                    </svg>
                    <div className="absolute inset-0 flex items-center justify-center text-white font-semibold text-sm">
                      {dayjs(goals[i].endDate).diff(dayjs(), 'day') > 0
                        ? `${dayjs(goals[i].endDate).diff(dayjs(), 'day')} days left`
                        : 'Done'}
                    </div>
                  </div>

                  <div className="text-lg font-medium text-center">{goals[i].goal}</div>
                </div>
              ) : (
                <button
                  key={i}
                  onClick={() => setShowGoalForm(true)} 
                  className="bg-blue-600 hover:bg-blue-700 text-white flex-1 h-44 rounded-2xl flex items-center justify-center text-xl font-semibold"
                >
                  + Add Goal
                </button>
              )
            )}
          </div>

          <div className="mt-12">
            <h2 className="text-2xl font-semibold mb-4 text-indigo-400">Time Distribution</h2>
            <div className="w-full h-[400px] overflow-visible">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={getTimeBlockChartData(timeblocks)}
                    dataKey="value"
                    nameKey="name"
                    cx="50%"
                    cy="50%"
                    outerRadius={140}
                    label={({ name, value }) => `${name} (${value}h)`}
                    fill="#8884d8"
                  >
                    {getTimeBlockChartData(timeblocks).map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>

          {showGoalForm && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
              <div className="bg-gray-800 p-6 rounded-xl shadow-lg w-96">
                <h2 className="text-2xl font-semibold text-white mb-4">Add New Goal</h2>
                <form onSubmit={handleAddGoal}>
                  <div className="mb-4">
                    <label htmlFor="goalName" className="text-white">Goal Name</label>
                    <input
                      type="text"
                      id="goalName"
                      value={goalName}
                      onChange={(e) => setGoalName(e.target.value)}
                      className="w-full p-2 mt-2 bg-gray-700 text-white rounded-md"
                    />
                  </div>
                  <div className="mb-4">
                    <label htmlFor="goalDate" className="text-white">End Date</label>
                    <input
                      type="date"
                      id="goalDate"
                      value={goalDate}
                      onChange={(e) => setGoalDate(e.target.value)}
                      className="w-full p-2 mt-2 bg-gray-700 text-white rounded-md"
                    />
                  </div>
                  <div className="flex justify-end gap-4">
                    <button
                      type="button"
                      onClick={() => setShowGoalForm(false)}
                      className="text-gray-400 hover:text-white"
                    >
                      Cancel
                    </button>
                    <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-md">
                      Add Goal
                    </button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;
