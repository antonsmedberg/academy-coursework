import React from 'react';

const TrainingButton = ({ onSubmit, isTraining, trainingData }) => {
  return (
    <button onClick={onSubmit} disabled={isTraining || !trainingData}>
      {isTraining ? 'Training in progress...' : 'Start Training'}
    </button>
  );
};

export default TrainingButton;

