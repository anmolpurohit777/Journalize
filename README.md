# 📘 Journalize — A Smart Productivity & AI Generated Journal Web App

Journalize is a productivity-focused web application designed to help users organize their tasks, plan their time, and reflect on their day — all in one place. Inspired by tools like Notion & Obsidian and enhanced with AI, Journalize combines daily planning features with automated journaling.

---

## 🧠 Key Features

-  **Urgency Matrix** — Prioritize tasks using the Eisenhower Method and move them between quadrants as needed.
-  **Kanban Board** — Visual task progression using drag-and-drop across Todo, In Progress, and Done.
-  **Time Blocker** — Plan your day hour-by-hour, with visual pie chart summary and overlap conflict warnings.
-  **Long-term Goal Countdown** — Motivational timers for upcoming personal or academic goals.
-  **AI-Powered Daily Journal** — Automatically generated journal at 10 PM, using your actual daily activity data.
-  **PDF Export** — Save your daily journal in a clean, formatted PDF for archiving or printing.

---

## 💻 Tech Stack

| Layer       | Technology                                      |
|-------------|-------------------------------------------------|
| Frontend    | React.js, TailwindCSS                           |
| Backend     | Spring Boot (Java)                              |
| Database    | MongoDB Atlas                                   |
| AI Engine   | Gemini API                                      |
| PDF Export  | jsPDF Library                                   |

---

## 📌 Scheduler Overview
The backend uses SpringScheduler to generate journals for all users at 10 PM daily.
It analyzes:

- Completed Todos

- Kanban tasks moved between columns

- Important tasks from Urgency Matrix

This data is used to generate a personalized journal entry per user, which is then saved and available to export as a PDF.

---

## 🔮 Future Enhancements

User login & authentication

- Mobile app version
- Weekly/Monthly journal summaries
- Pomodoro Timer for focused sessions
- Google Calendar sync
- Habit & Mood tracker
