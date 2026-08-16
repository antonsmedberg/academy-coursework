// api.js
const BASE_URL = 'http://localhost:5000';

export async function trainModel(modelName, trainingFramework, formData) {
  const url = `${BASE_URL}/train?model=${modelName}&framework=${trainingFramework}`;
  const response = await fetch(url, {
    method: 'POST',
    body: formData,
  });
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return response.json();
}
