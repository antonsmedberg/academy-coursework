import { createParticles } from './particles.js';
import { createConnections } from './connections.js';
import { animate } from './animation.js';
import { setupGUI } from './gui.js';

window.onerror = function(message, source, lineno, colno, error) {
    console.error("Error: ", message, "at", source, ":", lineno);
    alert("An error occurred. Please check the console for details.");
};

let scene, camera, renderer, controls, gui;
const defaultParams = {
    particleCount: 200,
    rotationSpeed: 0.0005,
    connectionThreshold: 5,
    particleSpeed: 0.005,
    particleSize: 0.1,
    lineOpacity: 0.2,
    colorCycle: false,
    attractionForce: 0.00001,
    particleColor: '#ffffff',
    connectionColor: '#ffffff',
    preset: 'Default'
};
const params = { ...defaultParams };

const presets = {
    'Default': { particleColor: '#ffffff', connectionColor: '#ffffff' },
    'Fire': { particleColor: '#ff4500', connectionColor: '#ff6347' },
    'Ice': { particleColor: '#00ffff', connectionColor: '#1e90ff' },
    'Nature': { particleColor: '#32cd32', connectionColor: '#228b22' }
};

/**
 * Initialize the scene, camera, and renderer.
 */
function init() {
    try {
        scene = new THREE.Scene();
        
        camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
        
        renderer = new THREE.WebGLRenderer({ antialias: true });
        if (!renderer.getContext()) {
            throw new Error("WebGL not supported");
        }
        renderer.setSize(window.innerWidth, window.innerHeight);
        renderer.setClearColor(0x000000);
        document.getElementById('container').appendChild(renderer.domElement);
        
        createParticles(scene, params);
        createConnections(scene, params);
        
        camera.position.z = 15;
        
        const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
        scene.add(ambientLight);
        
        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
        directionalLight.position.set(5, 5, 5);
        scene.add(directionalLight);
        
        controls = new THREE.OrbitControls(camera, renderer.domElement);
        controls.enableDamping = true;
        controls.dampingFactor = 0.05;
        controls.screenSpacePanning = false;
        controls.minDistance = 10;
        controls.maxDistance = 50;
        
        document.getElementById('loading').style.display = 'none';
        
        setupGUI(params, gui, presets, defaultParams);
        
        window.addEventListener('resize', onWindowResize, false);
        document.getElementById('fullscreen').addEventListener('click', toggleFullscreen, false);
        document.getElementById('themeToggle').addEventListener('click', toggleTheme, false);
        
        animate(scene, camera, renderer, controls, params);
    } catch (error) {
        console.error("Initialization error:", error);
        alert("Failed to initialize. Please check the console for details.");
    }
}

/**
 * Handle window resize event.
 */
function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

/**
 * Toggle fullscreen mode.
 */
function toggleFullscreen() {
    if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen();
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        }
    }
}

/**
 * Toggle dark mode.
 */
function toggleTheme() {
    document.body.classList.toggle('dark-mode');
}

document.addEventListener('DOMContentLoaded', init);







