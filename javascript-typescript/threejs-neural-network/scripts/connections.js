// connections.js
export let lineSystem;

export function createConnections(scene, params) {
    const geometry = new THREE.BufferGeometry();
    const linePositions = new Float32Array(params.particleCount * params.particleCount * 6);
    const lineColors = new Float32Array(params.particleCount * params.particleCount * 6);

    geometry.setAttribute('position', new THREE.BufferAttribute(linePositions, 3));
    geometry.setAttribute('color', new THREE.BufferAttribute(lineColors, 3));

    const material = new THREE.LineBasicMaterial({
        vertexColors: true,
        blending: THREE.AdditiveBlending,
        transparent: true,
        opacity: params.lineOpacity
    });

    lineSystem = new THREE.LineSegments(geometry, material);
    scene.add(lineSystem);
}

export function updateLineOpacity(value) {
    if (lineSystem) {
        lineSystem.material.opacity = value;
        lineSystem.material.needsUpdate = true;
    }
}

export function updateConnectionColor(value) {
    if (lineSystem) {
        const colors = lineSystem.geometry.attributes.color.array;
        const color = new THREE.Color(value);

        for (let i = 0; i < colors.length; i += 6) {
            colors[i] = colors[i + 3] = color.r;
            colors[i + 1] = colors[i + 4] = color.g;
            colors[i + 2] = colors[i + 5] = color.b;
        }
        lineSystem.geometry.attributes.color.needsUpdate = true;
    }
}







