import React, { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "./Sidebar";

const priorities = [
  { id: 1, label: "Urgent & Important", color: "text-red-600", border: "border-red-600" },
  { id: 2, label: "Not Urgent & Important", color: "text-yellow-600", border: "border-yellow-600" },
  { id: 3, label: "Urgent & Unimportant", color: "text-blue-600", border: "border-blue-600" },
  { id: 4, label: "Not Urgent & Unimportant", color: "text-emerald-600", border: "border-emerald-600" },
];

const UrgencyMatrixTab = () => {
  const userId = localStorage.getItem('userId');
  const [tasks, setTasks] = useState([]);
  const [draggedTask, setDraggedTask] = useState(null);

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/users/${userId}/matrix`);
      setTasks(res.data);
    } catch (err) {
      console.error("Failed to fetch tasks", err);
    }
  };

  const onDragStart = (task) => setDraggedTask(task);

  const onDrop = async (quadrant) => {
    if (!draggedTask || draggedTask.quadrant === quadrant) return;

    try {
      await axios.put(`http://localhost:8080/api/users/${userId}/matrix/${draggedTask.id}`, {
        ...draggedTask,
        quadrant,
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to update task", err);
    }
    setDraggedTask(null);
  };

  const handleCreateTask = async (quadrant) => {
    const description = prompt("Enter task description:");
    if (!description) return;
  
    try {
      await axios.post(`http://localhost:8080/api/users/${userId}/matrix`, {
        description,
        quadrant: quadrant,
      });
      fetchTasks();
    } catch (err) {
      console.error("Failed to create task", err);
    }
  };

  const handleToggle = async (task) => {
    try {
      const updated = { ...task, completed: !task.completed };
      await axios.put(`http://localhost:8080/api/users/${userId}/matrix/${task.id}`, updated);
      fetchTasks();
  
      if ((task.quadrant === 1 || task.quadrant === 2) && updated.completed) {
        const activity = `${task.description} marked as Completed`;
        await axios.post(`http://localhost:8080/api/users/${userId}/daily-logs`, {
          completedTodos: [],
          kanbanActivity: [],
          completedUrgentTasks: [activity],
          journalEntry: activity,
        });
      }
    } catch (err) {
      console.error("Failed to toggle task", err);
    }
  };
  

  const handleDelete = async (taskId) => {
    try {
      await axios.delete(`http://localhost:8080/api/users/${userId}/matrix/${taskId}`);
      fetchTasks();
    } catch (err) {
      console.error("Failed to delete task", err);
    }
  };

  const grouped = tasks.reduce((acc, task) => {
    const q = task.quadrant;
    if (!acc[q]) acc[q] = [];
    acc[q].push(task);
    return acc;
  }, {});

  return (
    <div className="flex h-screen bg-gray-950 text-gray-100">
      <Sidebar />

      <div className="flex-1 flex flex-col">
        <div className="flex-1 p-8 bg-gray-950 overflow-y-auto">
        <h1 className="text-3xl font-bold text-blue-400 mb-6 text-center">Urgency Matrix</h1>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {priorities.map((prio) => (
              <div
                key={prio.id}
                className={`rounded-2xl p-4 border ${prio.border} bg-gray-900 shadow-inner min-h-[250px]`}
                onDragOver={(e) => e.preventDefault()}
                onDrop={() => onDrop(prio.id)}
              >
                <div className="flex justify-between items-center mb-4">
                  <h2 className={`text-xl font-semibold ${prio.color}`}>{prio.label}</h2>
                  <button
                    onClick={() => handleCreateTask(prio.id)}
                    className="text-gray-400 hover:text-white text-lg"
                  >
                    ＋
                  </button>
                </div>

                <ul className="space-y-3">
                  {grouped[prio.id]?.map((task) => (
                    <li
                      key={task.id}
                      draggable
                      onDragStart={() => onDragStart(task)}
                      className={`bg-gray-800 border border-gray-700 rounded-xl px-4 py-3 flex justify-between items-center shadow-sm cursor-grab hover:shadow-md transition`}
                    >
                      <div className="flex items-center gap-2">
                        <input
                          type="checkbox"
                          checked={task.completed}
                          onChange={() => handleToggle(task)}
                          className="accent-blue-500"
                        />
                        <span className={task.completed ? "line-through text-gray-400" : ""}>
                          {task.description}
                        </span>
                      </div>
                      <button
                        onClick={() => handleDelete(task.id)}
                        className="text-gray-400 hover:text-white text-lg"
                      >
                        ✕
                      </button>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UrgencyMatrixTab;
