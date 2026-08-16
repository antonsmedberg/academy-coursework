import React, { useState } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './ModelTrainingComponent.css'; // Import your CSS file
import FileInput from './FileInput';
import DropZone from './DropZone';
import TrainingButton from './TrainingButton';

const ModelTrainingComponent = ({
  onTrainingComplete,
  modelName,
  trainingFramework,
}) => {
  const [trainingData, setTrainingData] = useState(null);
  const [isTraining, setIsTraining] = useState(false);
  const [fileName, setFileName] = useState('');
  const [isDragActive, setIsDragActive] = useState(false);

  const validFileTypes = ['text/csv', 'application/vnd.ms-excel'];
  const maxFileSize = 5 * 1024 * 1024;

  const handleFileChange = (file) => {
    setTrainingData(file);
  };

  const handleFileDrop = (file) => {
    processFile(file);
  };

  const processFile = (file) => {
    if (!validFileTypes.includes(file.type)) {
      toast.error('Please upload a valid file type (e.g., .csv)');
      return;
    }
    if (file.size > maxFileSize) {
      toast.error(`File size should not exceed ${maxFileSize / 1024 / 1024}MB`);
      return;
    }
    setFileName(file.name);
    setTrainingData(file);
  };

  const handleSubmit = async () => {
    if (!trainingData) {
      toast.error('Please select a file first!');
      return;
    }

    setIsTraining(true);

    try {
      // Customize the API endpoint and request parameters based on modelName and trainingFramework
      const formData = new FormData();
      formData.append('file', trainingData);
      const response = await fetch(`http://localhost:5000/train?model=${modelName}&framework=${trainingFramework}`, {
        method: 'POST',
        body: formData,
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      if (!result.modelId) {
        throw new Error('Invalid response from the server!');
      }
      toast.success('Training completed! Model ID: ' + result.modelId);
      onTrainingComplete(result.visualizationData);
    } catch (error) {
      console.error('Error during training:', error);
      toast.error('Training failed: ' + error.message);
    } finally {
      setIsTraining(false);
    }
  };

  return (
    <div className="modelTrainingComponent">
      <h2>Train {modelName} using {trainingFramework}</h2>
      <DropZone onFileDrop={handleFileDrop} isDragActive={isDragActive} isTraining={isTraining}>
        <FileInput onChange={handleFileChange} disabled={isTraining} />
      </DropZone>
      <TrainingButton onSubmit={handleSubmit} isTraining={isTraining} trainingData={trainingData} />
      {fileName && <div className="fileName">Selected file: {fileName}</div>}
    </div>
  );
};

export default ModelTrainingComponent;

