import { useEffect, useState } from 'react';
import axios from 'axios';
import Sidebar from './Sidebar';
import { jsPDF } from 'jspdf';

const cleanMarkdown = (markdown) => {
  return markdown
    .replace(/^##+\s*/gm, '')               
    .replace(/\*\*(.*?)\*\*/g, '**$1**')    
    .replace(/\*(.*?)\*/g, '*$1*')          
    .replace(/^\s*> /gm, '> ')              
    .trim();
};

const Journal = () => {
  const [journalEntry, setJournalEntry] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const userId = localStorage.getItem("userId");
  const date = new Date().toISOString().split('T')[0];

  const parseMarkdown = (markdown) => {
    const lines = markdown.split('\n');

    return lines.map((line, index) => {
      if (line.startsWith('# ')) {
        return <h1 key={index} className="text-4xl font-semibold text-purple-600 mb-4">{line.replace(/^# /, '')}</h1>;
      }
      else if (line.startsWith('## ')) {
        return <h2 key={index} className="text-3xl font-semibold text-teal-500 mb-4">{line.replace(/^## /, '')}</h2>;
      }
      else if (line.startsWith('### ')) {
        return <h3 key={index} className="text-2xl font-semibold text-yellow-400 mb-4">{line.replace(/^### /, '')}</h3>;
      }
      else if (line.startsWith('#### ')) {
        return <h4 key={index} className="text-xl font-semibold text-green-400 mb-4">{line.replace(/^#### /, '')}</h4>;
      }
      else if (line.startsWith('##### ')) {
        return <h5 key={index} className="text-lg font-semibold text-red-400 mb-4">{line.replace(/^##### /, '')}</h5>;
      }
      else if (line.startsWith('###### ')) {
        return <h6 key={index} className="text-md font-semibold text-purple-400 mb-4">{line.replace(/^###### /, '')}</h6>;
      }
      if (line.startsWith('>')) {
        return <blockquote key={index} className="text-green-400 italic mb-4">{line.replace(/^>\s?/, '')}</blockquote>;
      }
      if (line.startsWith('*')) {
        return <li key={index} className="ml-6 list-disc text-cyan-300 mb-1">{formatInline(line.replace(/^\*\s*/, ''))}</li>;
      }
      return <p key={index} className="mb-4 text-gray-300">{formatInline(line)}</p>;
    });
  };

  const formatInline = (text) => {
    text = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    text = text.replace(/\*(.*?)\*/g, '<em>$1</em>');

    return <span dangerouslySetInnerHTML={{ __html: text }} />;
  };

  const saveToPDF = () => {
    const doc = new jsPDF();
  
    const markdown = journalEntry; 
  
    const styles = {
      h1: { size: 22, color: [107, 33, 168] },
      h2: { size: 18, color: [20, 184, 166] },
      h3: { size: 16, color: [251, 191, 36] },
      h4: { size: 14, color: [52, 211, 153] },
      h5: { size: 12, color: [248, 113, 113] },
      h6: { size: 10, color: [168, 85, 247] },
      normal: { size: 12, color: [0, 0, 0] },
      listItem: { size: 12, color: [6, 182, 212] },
      blockquote: { size: 12, color: [52, 211, 153] },
    };
  
    const lines = markdown.split("\n");
    let yPosition = 10; 
    const lineHeight = 7;
    const pageWidth = doc.internal.pageSize.width - 20; 
    const pageHeight = doc.internal.pageSize.height;
  
    const addText = (text, style) => {
      doc.setTextColor(style.color[0], style.color[1], style.color[2]);
      doc.setFontSize(style.size);
  
      const wrappedText = doc.splitTextToSize(text, pageWidth);
      const textHeight = wrappedText.length * lineHeight;
  
      if (yPosition + textHeight > pageHeight - 20) {
        doc.addPage();
        yPosition = 10;
      }
  
      doc.text(wrappedText, 10, yPosition);
      yPosition += textHeight; 
    };
  
    lines.forEach((line) => {
      if (line.startsWith("# ")) {
        addText(line.replace(/^# /, ""), styles.h1);
      } else if (line.startsWith("## ")) {
        addText(line.replace(/^## /, ""), styles.h2);
      } else if (line.startsWith("### ")) {
        addText(line.replace(/^### /, ""), styles.h3);
      } else if (line.startsWith("#### ")) {
        addText(line.replace(/^#### /, ""), styles.h4);
      } else if (line.startsWith("##### ")) {
        addText(line.replace(/^##### /, ""), styles.h5);
      } else if (line.startsWith("###### ")) {
        addText(line.replace(/^###### /, ""), styles.h6);
      } else if (line.startsWith("> ")) {
        addText(line.replace(/^> /, ""), styles.blockquote);
      } else if (line.startsWith("* ")) {
        addText(line.replace(/^\* /, ""), styles.listItem);
      } else {
        addText(line, styles.normal);
      }
    });
  
    doc.save("journal-entry.pdf");
  };
  

  useEffect(() => {
    const fetchJournalData = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/users/${userId}/daily-logs/${date}`);
        const cleaned = cleanMarkdown(response.data.journalEntry);
        setJournalEntry(cleaned);
        setLoading(false);
      } catch (err) {
        setError('Error fetching journal data');
        setLoading(false);
      }
    };

    fetchJournalData();
  }, [userId, date]);

  if (loading || error) {
    return (
      <div className="flex h-screen bg-gray-950 text-gray-100">
        <Sidebar />
        <div className="flex-1 flex flex-col items-center justify-center">
          <p className="text-lg text-gray-400">
            {loading ? 'Loading journal...' : error}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-950 text-gray-100">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-y-auto relative">
        <header className="lg:hidden p-4 bg-gray-900 text-white flex items-center justify-between shadow-md">
          <div className="font-semibold text-lg">Journal</div>
        </header>

        <button
          onClick={saveToPDF}
          className="absolute top-4 right-4 px-4 py-2 bg-purple-600 text-white rounded-lg shadow-lg hover:bg-purple-700 transition duration-200"
        >
          Save as PDF
        </button>

        <div className="flex-1 p-8 bg-gray-950">
        <h1 className="text-3xl font-bold text-blue-400 mb-6 text-center">Today's Journal 📝</h1>
          <p className="text-lg text-gray-400 mb-8 text-center">A recap of the day based on your tasks, progress, and reflections.</p>

          <div className="prose prose-invert max-w-none bg-gray-900 p-6 rounded-2xl shadow-inner border border-gray-800 overflow-x-auto">
            <div className="p-6 bg-gray-900 rounded-2xl text-gray-100 journal-content">
              {parseMarkdown(journalEntry)}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Journal;
