import React from 'react';

const FileInput = ({ onChange, disabled }) => {
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    onChange(file);
  };

  return (
    <input type="file" onChange={handleFileChange} disabled={disabled} />
  );
};

export default FileInput;

