import React, { useState } from 'react';

const DropZone = ({ children, onFileDrop, isTraining }) => {
  const [isDragActive, setIsDragActive] = useState(false); // Define setIsDragActive here

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const handleDragIn = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (!isTraining) {
      setIsDragActive(true);
    }
  };

  const handleDragOut = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragActive(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragActive(false);

    if (!isTraining && e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      const file = e.dataTransfer.files[0];
      onFileDrop(file);
      e.dataTransfer.clearData();
    }
  };

  return (
    <div
      className={`file-dropzone ${isDragActive ? 'active' : ''}`}
      onDragOver={handleDrag}
      onDragEnter={handleDragIn}
      onDragLeave={handleDragOut}
      onDrop={handleDrop}
    >
      {children}
      <div className="drop-message">
        {isDragActive ? 'Drop the files here...' : `Drag 'n' drop some files here, or click to select files`}
      </div>
    </div>
  );
};

export default DropZone;

