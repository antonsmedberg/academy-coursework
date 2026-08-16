// animation.js
import { particleSystem } from './particles.js';
import { lineSystem } from './connections.js';

export function animate(scene, camera, renderer, controls, params) {
    requestAnimationFrame(() => animate(scene, camera, renderer, controls, params));

    updateParticles(params);
    updateConnections(params);

    rotateSystems(params);
    controls.update();
    renderer.render(scene, camera);
}

function rotateSystems(params) {
    if (particleSystem && lineSystem) {
        particleSystem.rotation.x += params.rotationSpeed;
        particleSystem.rotation.y += params.rotationSpeed;
        lineSystem.rotation.x += params.rotationSpeed;
        lineSystem.rotation.y += params.rotationSpeed;
    }
}

function updateParticles(params) {
    if (particleSystem) {
        const positions = particleSystem.geometry.attributes.position.array;
        const colors = particleSystem.geometry.attributes.color.array;
        const time = Date.now() * 0.001;

        for (let i = 0; i < positions.length; i += 3) {
            updateParticlePosition(i, positions, params);
            applyColorCycle(i, colors, time, params);
        }
        particleSystem.geometry.attributes.position.needsUpdate = true;
        particleSystem.geometry.attributes.color.needsUpdate = true;
    }
}

function updateParticlePosition(index, positions, params) {
    positions[index] += (Math.random() - 0.5) * params.particleSpeed;
    positions[index + 1] += (Math.random() - 0.5) * params.particleSpeed;
    positions[index + 2] += (Math.random() - 0.5) * params.particleSpeed;

    positions[index] += -positions[index] * params.attractionForce;
    positions[index + 1] += -positions[index + 1] * params.attractionForce;
    positions[index + 2] += -positions[index + 2] * params.attractionForce;

    if (Math.abs(positions[index]) > 5) positions[index] *= -0.9;
    if (Math.abs(positions[index + 1]) > 5) positions[index + 1] *= -0.9;
    if (Math.abs(positions[index + 2]) > 5) positions[index + 2] *= -0.9;
}

function applyColorCycle(index, colors, time, params) {
    if (params.colorCycle) {
        colors[index] = Math.sin(time + index) * 0.5 + 0.5;
        colors[index + 1] = Math.sin(time + index + 2) * 0.5 + 0.5;
        colors[index + 2] = Math.sin(time + index + 4) * 0.5 + 0.5;
    }
}

function updateConnections(params) {
    if (lineSystem && particleSystem) {
        const positions = particleSystem.geometry.attributes.position.array;
        const colors = particleSystem.geometry.attributes.color.array;
        const linePositions = lineSystem.geometry.attributes.position.array;
        const lineColors = lineSystem.geometry.attributes.color.array;
        let lineIndex = 0;

        for (let i = 0; i < params.particleCount; i++) {
            for (let j = i + 1; j < params.particleCount; j++) {
                updateConnection(i, j, positions, colors, linePositions, lineColors, lineIndex, params);
                lineIndex += 6;
            }
        }
        lineSystem.geometry.attributes.position.needsUpdate = true;
        lineSystem.geometry.attributes.color.needsUpdate = true;
    }
}

function updateConnection(i, j, positions, colors, linePositions, lineColors, lineIndex, params) {
    const dx = positions[i * 3] - positions[j * 3];
    const dy = positions[i * 3 + 1] - positions[j * 3 + 1];
    const dz = positions[i * 3 + 2] - positions[j * 3 + 2];
    const distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

    if (distance < params.connectionThreshold) {
        setLineAttributes(i, j, positions, colors, linePositions, lineColors, lineIndex, distance, params);
    } else {
        clearLineAttributes(linePositions, lineColors, lineIndex);
    }
}

function setLineAttributes(i, j, positions, colors, linePositions, lineColors, lineIndex, distance, params) {
    linePositions[lineIndex] = positions[i * 3];
    linePositions[lineIndex + 1] = positions[i * 3 + 1];
    linePositions[lineIndex + 2] = positions[i * 3 + 2];
    linePositions[lineIndex + 3] = positions[j * 3];
    linePositions[lineIndex + 4] = positions[j * 3 + 1];
    linePositions[lineIndex + 5] = positions[j * 3 + 2];

    const alpha = 1 - (distance / params.connectionThreshold);
    lineColors[lineIndex] = colors[i * 3] * alpha;
    lineColors[lineIndex + 1] = colors[i * 3 + 1] * alpha;
    lineColors[lineIndex + 2] = colors[i * 3 + 2] * alpha;
    lineColors[lineIndex + 3] = colors[j * 3] * alpha;
    lineColors[lineIndex + 4] = colors[j * 3 + 1] * alpha;
    lineColors[lineIndex + 5] = colors[j * 3 + 2] * alpha;
}

function clearLineAttributes(linePositions, lineColors, lineIndex) {
    linePositions[lineIndex] = linePositions[lineIndex + 1] = linePositions[lineIndex + 2] = 0;
    linePositions[lineIndex + 3] = linePositions[lineIndex + 4] = linePositions[lineIndex + 5] = 0;
    lineColors[lineIndex] = lineColors[lineIndex + 1] = lineColors[lineIndex + 2] = 0;
    lineColors[lineIndex + 3] = lineColors[lineIndex + 4] = lineColors[lineIndex + 5] = 0;
}

