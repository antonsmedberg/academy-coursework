// particles.js
export let particleSystem;

export function createParticles(scene, params) {
    const geometry = new THREE.BufferGeometry();
    const positions = new Float32Array(params.particleCount * 3);
    const colors = new Float32Array(params.particleCount * 3);
    const sizes = new Float32Array(params.particleCount);

    for (let i = 0; i < params.particleCount; i++) {
        const i3 = i * 3;
        positions[i3] = (Math.random() - 0.5) * 10;
        positions[i3 + 1] = (Math.random() - 0.5) * 10;
        positions[i3 + 2] = (Math.random() - 0.5) * 10;

        const color = new THREE.Color(params.particleColor);
        colors[i3] = color.r;
        colors[i3 + 1] = color.g;
        colors[i3 + 2] = color.b;

        sizes[i] = Math.random() * 0.1 + 0.05;
    }

    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('color', new THREE.BufferAttribute(colors, 3));
    geometry.setAttribute('size', new THREE.BufferAttribute(sizes, 1));

    const material = new THREE.PointsMaterial({
        size: params.particleSize,
        vertexColors: true,
        sizeAttenuation: true,
        alphaTest: 0.5,
        transparent: true
    });

    particleSystem = new THREE.Points(geometry, material);
    scene.add(particleSystem);
}

export function updateParticleSize(value) {
    if (particleSystem) {
        particleSystem.material.size = value;
        particleSystem.material.needsUpdate = true;
    }
}

export function updateParticleColor(value) {
    if (particleSystem) {
        const colors = particleSystem.geometry.attributes.color.array;
        const color = new THREE.Color(value);

        for (let i = 0; i < colors.length; i += 3) {
            colors[i] = color.r;
            colors[i + 1] = color.g;
            colors[i + 2] = color.b;
        }
        particleSystem.geometry.attributes.color.needsUpdate = true;
    }
}


