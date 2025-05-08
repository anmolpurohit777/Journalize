import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Sidebar from './Sidebar';

const TodoTab = () => {
  const [todos, setTodos] = useState([]);
  const [input, setInput] = useState('');

  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const userId = localStorage.getItem('userId');

      if (!userId) {
        console.log('No ID found in localStorage');
        return;
      }

      const response = await axios.get(`http://localhost:8080/api/users/${userId}/todos`);
      setTodos(response.data);
    } catch (error) {
      console.error('Error fetching todos:', error);
    }
  };

  const handleAddOrUpdate = async () => {
    const userId = localStorage.getItem('userId');

      if (!userId) {
        console.log('No ID found in localStorage');
        return;
      }
   
    if (!input.trim()) return;
    try {
        await axios.post(`http://localhost:8080/api/users/${userId}/todos`, {
          description: input, 
          completed: false,
        });
      setInput('');
      fetchTodos();
    } catch (error) {
      console.error('Error adding or updating todo:', error);
    }
  };

  const handleToggleComplete = async (todo) => {
    const userId = localStorage.getItem('userId');

      if (!userId) {
        console.log('No ID found in localStorage');
        return;
      }
    try {
      await axios.put(`http://localhost:8080/api/users/${userId}/todos/${todo.id}`, {
        description : todo.description,
        completed: !todo.completed,
      });

      await axios.post(`http://localhost:8080/api/users/${userId}/daily-logs`, {
        completedTodos: [todo.description],
        kanbanActivity: [],                  
        completedUrgentTasks: [],           
        journalEntry: "",
      });

      fetchTodos();
    } catch (error) {
      console.error("Error updating todo:", error);
    }
  };

  const handleDelete = async (id) => {
    const userId = localStorage.getItem('userId');

      if (!userId) {
        console.log('No ID found in localStorage');
        return;
      }
    try {
      await axios.delete(`http://localhost:8080/api/users/${userId}/todos/${id}`);
      fetchTodos();
    } catch (error) {
      console.error("Error deleting todo:", error);
    }
  };

  return (
    <div className="flex h-screen bg-gray-950 text-gray-100">
      <Sidebar />
  
      <div className="flex-1 flex flex-col items-center p-8 overflow-y-auto">
        <div className="w-full max-w-2xl">
          <h1 className="text-3xl font-bold text-blue-400 mb-6 text-center">Todos</h1>
  
          {/* Input */}
          <div className="flex items-center gap-2 mb-6">
            <input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Add a task..."
              className="flex-1 px-3 py-2 rounded-md bg-gray-800 text-white border border-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
            />
            <button
              onClick={handleAddOrUpdate}
              className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-2 rounded-md text-sm transition"
            >
              Add
            </button>
          </div>
  
          <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
          {/* Incomplete Tasks */}
          <div>
            <h2 className="text-lg font-semibold text-white mb-3">Incomplete Tasks</h2>
            <ul className="space-y-2">
              {todos.filter(todo => !todo.completed).map((todo) => (
                <li
                  key={todo.id}
                  className="flex items-center justify-between px-3 py-2 bg-gray-800 rounded-md border border-gray-700 shadow-sm"
                >
                  <label className="flex items-center gap-2 cursor-pointer w-full">
                    <input
                      type="checkbox"
                      checked={todo.completed}
                      onChange={() => handleToggleComplete(todo)}
                      className="form-checkbox h-4 w-4 rounded border-gray-600 text-blue-500 bg-gray-900"
                    />
                    <span className="flex-1 text-sm">{todo.description}</span>
                  </label>
                  <button
                    onClick={() => handleDelete(todo.id)}
                    className="text-lg px-2 text-red-400 hover:text-red-600 transition"
                    aria-label="Delete"
                  >
                    ×
                  </button>
                </li>
              ))}
            </ul>
          </div>

          {/* Completed Tasks */}
          <div>
            <h2 className="text-lg font-semibold text-white mb-3">Completed Tasks</h2>
            <ul className="space-y-2">
              {todos.filter(todo => todo.completed).map((todo) => (
                <li
                  key={todo.id}
                  className="flex items-center justify-between px-3 py-2 bg-gray-800 rounded-md border border-gray-700 shadow-sm"
                >
                  <label className="flex items-center gap-2 cursor-pointer w-full">
                    <input
                      type="checkbox"
                      checked={todo.completed}
                      onChange={() => handleToggleComplete(todo)}
                      className="form-checkbox h-4 w-4 rounded border-gray-600 text-blue-500 bg-gray-900"
                    />
                    <span className="flex-1 line-through text-gray-500 text-sm">{todo.description}</span>
                  </label>
                  <button
                    onClick={() => handleDelete(todo.id)}
                    className="text-lg px-2 text-red-400 hover:text-red-600 transition"
                    aria-label="Delete"
                  >
                    ×
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </div>
        </div>
      </div>
    </div>
  );
  
};

export default TodoTab;
