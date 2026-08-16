import React, { useRef, useEffect, useMemo } from 'react';
import Chart from 'chart.js/auto';
import './VisualizationComponent.css';

const VisualizationComponent = ({ data }) => {
  const chartRef = useRef(null);
  const canvasRef = useRef(null);

  const chartData = useMemo(() => ({
    labels: data.labels,
    datasets: [{
      label: 'Model Accuracy',
      data: data.values,
      fill: false,
      backgroundColor: 'rgb(75, 192, 192)',
      borderColor: 'rgba(75, 192, 192, 0.2)',
    }],
  }), [data]);

  useEffect(() => {
    const createOrUpdateChart = () => {
      if (chartRef.current) {
        chartRef.current.destroy();
      }
  
      const ctx = canvasRef.current.getContext('2d');
      chartRef.current = new Chart(ctx, {
        type: 'line',
        data: chartData,
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: 'rgba(0, 0, 0, 0.1)',
                borderColor: 'rgba(0, 0, 0, 0.1)',
              },
            },
            x: {
              grid: {
                display: false,
              },
            },
          },
          elements: {
            line: {
              tension: 0.4,
              borderWidth: 3,
            },
            point: {
              radius: 5,
              backgroundColor: 'rgb(75, 192, 192)',
              borderColor: 'rgba(75, 192, 192, 1)',
            },
          },
          plugins: {
            legend: {
              display: true,
              position: 'top',
              labels: {
                color: '#333',
                font: {
                  size: 14,
                },
              },
            },
          },
        },
      });
    };
  
    createOrUpdateChart();
  
    return () => {
      if (chartRef.current) {
        chartRef.current.destroy();
      }
    };
  }, [data, chartData]);

  return (
    <div className="visualizationComponent">
      <h2>Model Accuracy Over Time</h2>
      <div className="chart-container">
        <canvas ref={canvasRef} id="chartCanvas"></canvas>
      </div>
    </div>
  );
};

export default VisualizationComponent;

