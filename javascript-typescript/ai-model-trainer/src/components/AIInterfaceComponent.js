// AIInterfaceComponent.js

import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify'; // Import ToastContainer
import 'react-toastify/dist/ReactToastify.css';
import ModelTrainingComponent from './ModelTrainingComponent';
import VisualizationComponent from './VisualizationComponent';
import './AIInterfaceComponent.css'; // Import your CSS file



const AIInterfaceComponent = () => {
  const [visualizationData, setVisualizationData] = useState({
    labels: [],
    values: [],
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

// Inside your component
const updateVisualizationData = async (modelId) => {
  setIsLoading(true);
  setError(null);
  try {
    // Fetch new visualization data using the model ID
    const response = await fetch(`http://localhost:5000/visualization/${modelId}`);
    if (!response.ok) throw new Error('Failed to fetch visualization data');
    const newData = await response.json();
    setVisualizationData(newData);
    toast.success('Data updated successfully'); // Show a success toast
  } catch (err) {
    setError(err.message);
    toast.error('Error updating data'); // Show an error toast
  } finally {
    setIsLoading(false);
  }
};


return (
  <div className="aiInterfaceComponent">
    <ToastContainer />
    <h1>AI Model Interface</h1>
    <p>Welcome to the AI Model Training Interface.</p>
    <ModelTrainingComponent onTrainingComplete={updateVisualizationData} />
    {error && <div className="error">{error}</div>}
    {isLoading ? (
      <div className="loading-spinner"></div>
    ) : (
      <VisualizationComponent data={visualizationData} />
    )}
  </div>
);

};

export default AIInterfaceComponent;

