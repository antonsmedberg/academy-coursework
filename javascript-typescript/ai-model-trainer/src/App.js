import React from 'react';
import './App.css';
import './VisualizationComponent.css'; // Import the new CSS
import AIInterfaceComponent from './components/AIInterfaceComponent';
import VisualizationComponent from './components/VisualizationComponent';

// Dummy data for visualization (replace with real data)
const dummyData = {
  labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
  values: [65, 59, 80, 81, 56, 55, 40],
};

// App.js
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>AI Interface</h1>
      </header>
      <div className="App-content"> {/* New content wrapper */}
        <AIInterfaceComponent />
        <VisualizationComponent data={dummyData} />
      </div>
    </div>
  );
}

export default App;



