import React from 'react';
import { createRoot } from 'react-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

// Remove this line: toast.configure();

const root = createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <ToastContainer />
    <App />
  </React.StrictMode>
);

reportWebVitals();





