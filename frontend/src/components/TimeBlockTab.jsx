import React, { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "./Sidebar";

const timeOptions = Array.from({ length: 35 }, (_, i) => {
  const totalMinutes = 300 + i * 30;
  const h = Math.floor(totalMinutes / 60).toString().padStart(2, "0");
  const m = (totalMinutes % 60).toString().padStart(2, "0");
  return `${h}:${m}`;
});

const parseTime = (time) => {
  const [h, m] = time.split(":").map(Number);
  return h * 60 + m;
};

const TimeBlockTab = () => {
  const userId = localStorage.getItem("userId");
  const [blocks, setBlocks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [description, setDescription] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  useEffect(() => {
    fetchBlocks();
  }, []);

  const fetchBlocks = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/users/${userId}/timeblocks`);
      setBlocks(res.data);
    } catch (err) {
      console.error("Error fetching time blocks", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/users/${userId}/timeblocks/${id}`);
      fetchBlocks();
    } catch (err) {
      console.error("Error deleting block", err);
    }
  };

  const checkOverlap = (start, end) => {
    const newStart = parseTime(start);
    const newEnd = parseTime(end);
    return blocks.some(
      (b) => newStart < parseTime(b.endTime) && newEnd > parseTime(b.startTime)
    );
  };

  const handleSave = async () => {
    if (!description || !startTime || !endTime) return alert("All fields required");
    if (parseTime(startTime) >= parseTime(endTime)) return alert("End time must be after start time");
    if (checkOverlap(startTime, endTime)) return alert("Time block overlaps with existing one");

    try {
      await axios.post(`http://localhost:8080/api/users/${userId}/timeblocks`, {
        description,
        startTime,
        endTime,
      });
      fetchBlocks();
      setShowModal(false);
      setDescription("");
      setStartTime("");
      setEndTime("");
    } catch (err) {
      console.error("Error creating block", err);
    }
  };

  return (
    <div className="flex h-screen bg-gray-950 text-white">
      <Sidebar />
  
      <div className="flex-1 p-8 overflow-y-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-blue-400 mb-6 text-center flex-1">Time Blocks</h1>
        <button
          onClick={() => setShowModal(true)}
          className="px-4 py-2 bg-emerald-600 text-white rounded-xl hover:bg-emerald-700"
        >
          ＋ Add Block
        </button> 
      </div>
        <div className="relative border-l border-gray-700 h-[1083px] ml-8">
          {timeOptions.map((time) => (
            <div
              key={time}
              className="h-8 border-b border-gray-800 text-gray-500 text-sm pl-2"
            >
              {time}
            </div>
          ))}
  
          {blocks.map((block) => {
            const top = ((parseTime(block.startTime) - 300) / 30) * 32;
            const height =
              ((parseTime(block.endTime) - parseTime(block.startTime)) / 30) * 32;
  
            return (
              <div
                key={block.id}
                className="absolute left-48 w-128 bg-blue-600/30 border border-blue-400/40 text-blue-100 rounded-lg px-3 py-2 text-sm backdrop-blur"
                style={{ top: `${top}px`, height: `${height}px` }}
              >
                <div className="flex justify-between items-start gap-2">
                  <div className="truncate">
                    <div className="font-semibold">{block.description}</div>
                    <div className="text-xs opacity-70">
                      {block.startTime} – {block.endTime}
                    </div>
                  </div>
                  <button
                    onClick={() => handleDelete(block.id)}
                    className="text-white text-sm hover:text-red-400"
                  >
                    ✕
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      </div>
  
      {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-60 z-50">
          <div className="bg-gray-900 rounded-2xl p-6 w-96 shadow-xl border border-gray-700">
            <h2 className="text-xl font-semibold text-white mb-4">Create Time Block</h2>
  
            <label className="block mb-2 text-sm text-gray-400">Description</label>
            <input
              type="text"
              className="w-full px-3 py-2 mb-4 rounded-lg bg-gray-800 text-white border border-gray-700 focus:outline-none"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
  
            <label className="block mb-2 text-sm text-gray-400">Start Time</label>
            <select
              className="w-full px-3 py-2 mb-4 rounded-lg bg-gray-800 text-white border border-gray-700"
              value={startTime}
              onChange={(e) => {
                setStartTime(e.target.value);
                setEndTime("");
              }}
            >
              <option value="">Select</option>
              {timeOptions.map((time) => (
                <option key={time} value={time}>
                  {time}
                </option>
              ))}
            </select>
  
            <label className="block mb-2 text-sm text-gray-400">End Time</label>
            <select
              className="w-full px-3 py-2 mb-6 rounded-lg bg-gray-800 text-white border border-gray-700"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              disabled={!startTime}
            >
              <option value="">Select</option>
              {timeOptions
                .filter((t) => parseTime(t) > parseTime(startTime))
                .map((time) => (
                  <option key={time} value={time}>
                    {time}
                  </option>
                ))}
            </select>
  
            <div className="flex justify-end gap-4">
              <button
                className="px-4 py-2 rounded-xl bg-gray-700 hover:bg-gray-600 text-white"
                onClick={() => setShowModal(false)}
              >
                Cancel
              </button>
              <button
                className="px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white"
                onClick={handleSave}
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
  
  
};

export default TimeBlockTab;
