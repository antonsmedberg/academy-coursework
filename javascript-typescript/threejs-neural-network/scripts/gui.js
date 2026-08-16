// gui.js
import { updateParticleSize, updateParticleColor } from './particles.js';
import { updateLineOpacity, updateConnectionColor } from './connections.js';

export function setupGUI(params, gui, presets, defaultParams) {
    gui = new dat.GUI({ width: 350 });
    
    const generalFolder = gui.addFolder('General Settings');
    generalFolder.add(params, 'rotationSpeed', 0, 0.01).name('Rotation Speed').listen();
    generalFolder.add(params, 'connectionThreshold', 1, 10).name('Connection Threshold').listen();
    generalFolder.add(params, 'particleSpeed', 0, 0.05).name('Particle Speed').listen();
    generalFolder.add(params, 'colorCycle').name('Cycle Colors').listen();
    generalFolder.add(params, 'attractionForce', 0, 0.0001).name('Attraction Force').listen();
    generalFolder.open();

    const appearanceFolder = gui.addFolder('Appearance Settings');
    appearanceFolder.add(params, 'particleSize', 0.01, 0.5).name('Particle Size').onChange(updateParticleSize).listen();
    appearanceFolder.add(params, 'lineOpacity', 0, 1).name('Line Opacity').onChange(updateLineOpacity).listen();
    appearanceFolder.addColor(params, 'particleColor').name('Particle Color').onChange(updateParticleColor).listen();
    appearanceFolder.addColor(params, 'connectionColor').name('Connection Color').onChange(updateConnectionColor).listen();
    appearanceFolder.open();

    const presetFolder = gui.addFolder('Presets');
    presetFolder.add(params, 'preset', Object.keys(presets)).name('Preset').onChange(value => loadPreset(value, params, gui, presets, defaultParams));

    const saveLoadFolder = gui.addFolder('Save/Load Settings');
    saveLoadFolder.add({ save: () => saveSettings(params) }, 'save').name('Save Settings');
    saveLoadFolder.add({ load: () => loadSettings(params, gui) }, 'load').name('Load Settings');
    saveLoadFolder.add({ reset: () => resetParams(params, gui, defaultParams) }, 'reset').name('Reset to Default');
}

function loadPreset(value, params, gui, presets, defaultParams) {
    const preset = presets[value];
    if (preset) {
        params.particleColor = preset.particleColor;
        params.connectionColor = preset.connectionColor;
        gui.updateDisplay();
        updateParticleColor(params.particleColor);
        updateConnectionColor(params.connectionColor);
    }
}

function resetParams(params, gui, defaultParams) {
    Object.assign(params, defaultParams);
    gui.updateDisplay();
    updateParticleSize(params.particleSize);
    updateLineOpacity(params.lineOpacity);
    updateParticleColor(params.particleColor);
    updateConnectionColor(params.connectionColor);
}

function saveSettings(params) {
    const settings = JSON.stringify(params);
    localStorage.setItem('particleSettings', settings);
    alert('Settings saved!');
}

function loadSettings(params, gui) {
    const settings = localStorage.getItem('particleSettings');
    if (settings) {
        Object.assign(params, JSON.parse(settings));
        gui.updateDisplay();
        updateParticleSize(params.particleSize);
        updateLineOpacity(params.lineOpacity);
        updateParticleColor(params.particleColor);
        updateConnectionColor(params.connectionColor);
        alert('Settings loaded!');
    } else {
        alert('No settings found!');
    }
}





