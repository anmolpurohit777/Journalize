import { useEffect, useState } from 'react';
import Sidebar from './Sidebar';
import axios from 'axios';

const columns = ['To-Do', 'In Progress', 'Completed'];

const KanbanTab = () => {
  const [kanbans, setKanbans] = useState([]);

  useEffect(() => {
    const userId = localStorage.getItem('userId');

    axios.get(`http://localhost:8080/api/users/${userId}/kanbans`)
      .then(res => setKanbans(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleAddCard = (status) => {
    const userId = localStorage.getItem('userId');

    const title = prompt("Enter card title:");
    if (!title) return;

    axios.post(`http://localhost:8080/api/users/${userId}/kanbans`, {
      description: title,
      status
    }).then(res => setKanbans(prev => [...prev, res.data]));
  };

  const handleDeleteCard = (id) => {
    const userId = localStorage.getItem('userId');

    axios.delete(`http://localhost:8080/api/users/${userId}/kanbans/${id}`)
      .then(() => setKanbans(prev => prev.filter(card => card.id !== id)));
  };

  const handleDragStart = (e, cardId) => {
    e.dataTransfer.setData('cardId', cardId);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDrop = (e, status) => {
    const cardId = e.dataTransfer.getData('cardId');
    const userId = localStorage.getItem('userId');

    axios.put(`http://localhost:8080/api/users/${userId}/kanbans/${cardId}`, {
      status
    }).then(() => {
      setKanbans(prev => prev.map(card =>
        card.id === cardId ? { ...card, status } : card
      ));
    }).catch(err => console.error(err));
  };

  return (
    <div className="flex h-screen bg-gray-950 text-white">
      <Sidebar />
  
      <main className="flex-1 p-6 overflow-x-auto">
        <h1 className="text-3xl font-bold text-blue-400 mb-6 text-center">Kanban Board</h1>
  
        <div className="flex justify-center gap-6">
          {columns.map(col => (
            <div
              key={col}
              className="bg-gray-900 w-72 rounded-lg p-4 shadow-md border border-gray-800 flex flex-col"
              onDragOver={handleDragOver}
              onDrop={(e) => handleDrop(e, col)}
            >
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-lg font-semibold">{col}</h2>
                <span className="text-sm text-gray-400">
                  {kanbans.filter(k => k.status === col).length}
                </span>
              </div>

              <div className="flex-1 space-y-2 overflow-y-auto">
                {kanbans
                  .filter(k => k.status === col)
                  .map(k => (
                    <div
                      key={k.id}
                      className="bg-gray-800 p-3 rounded-md text-sm flex justify-between items-center"
                      draggable
                      onDragStart={(e) => handleDragStart(e, k.id)}
                    >
                      <span>{k.description}</span>
                      <button
                        onClick={() => handleDeleteCard(k.id)}
                        className="text-gray-400 hover:text-red-500 text-sm"
                      >
                        ✕
                      </button>
                    </div>
                  ))}
              </div>
  
              <button
                onClick={() => handleAddCard(col)}
                className="mt-4 text-sm bg-gray-700 hover:bg-gray-600 text-white py-1 rounded-md"
              >
                +Add a card
              </button>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};

export default KanbanTab;
