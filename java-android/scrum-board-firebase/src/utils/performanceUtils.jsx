// src/utils/performanceUtils.jsx

/**
 * Min prestandadetektiv - håller koll på hur snabb appen är! 🕵️‍♂️
 *
 * Som en personlig tränare för din app som mäter:
 * - Hur snabbt komponenter renderas (som en stoppur)
 * - Hur mycket minne som används (så vi inte äter upp allt)
 * - Hur stor appen blir (ingen vill ha en långsam jätte)
 * - Hur responsiv allt känns för användaren
 *
 * Perfekt för att hitta var appen blir seg och fixa det!
 */

/**
 * Mäter renderingstid för komponenter ⏱️
 *
 * Varnar om rendering tar längre än 16ms (60fps-gränsen).
 *
 * @param {string} componentName - Komponentnamn
 * @param {Function} renderFunction - Render-funktion
 * @returns {any} Funktionsresultat med timing
 */
export const measureRenderTime = (componentName, renderFunction) => {
  if (process.env.NODE_ENV !== "development") {
    return renderFunction();
  }

  const startTime = performance.now();
  const result = renderFunction();
  const endTime = performance.now();

  const renderTime = endTime - startTime;

  // Varna om långsam rendering
  if (renderTime > 16) {
    console.warn(
      `🐌 Slow render detected: ${componentName} took ${renderTime.toFixed(
        2
      )}ms`
    );
  } else {
    console.log(`⚡ ${componentName} rendered in ${renderTime.toFixed(2)}ms`);
  }

  return result;
};

/**
 * Lindar in komponenter för prestandamätning 📹
 */
export const withPerformanceProfiler = (WrappedComponent, componentName) => {
  return function ProfiledComponent(props) {
    return measureRenderTime(componentName, () => (
      <WrappedComponent {...props} />
    ));
  };
};

/**
 * Minnesmätare - kollar så appen inte äter upp allt minne 🧠
 *
 * Som att kolla hur mycket plats som finns kvar i datorn.
 * Varnar om vi börjar använda för mycket (över 80%).
 */
export const measureMemoryUsage = () => {
  if (!performance.memory) {
    console.warn("Memory measurement not supported in this browser");
    return null;
  }

  const memory = performance.memory;
  const usedMB = Math.round(memory.usedJSHeapSize / 1048576);
  const totalMB = Math.round(memory.totalJSHeapSize / 1048576);
  const limitMB = Math.round(memory.jsHeapSizeLimit / 1048576);

  const memoryInfo = {
    used: usedMB,
    total: totalMB,
    limit: limitMB,
    usage: Math.round((usedMB / limitMB) * 100),
  };

  // Varnar om vi börjar bli minneshungriga
  if (memoryInfo.usage > 80) {
    console.warn(
      `🚨 High memory usage: ${memoryInfo.usage}% (${usedMB}MB/${limitMB}MB)`
    );
  }

  return memoryInfo;
};

/**
 * Mäter laddningstider för async-operationer ⏰
 *
 * Sparar resultat för prestandaanalys.
 */
export const measureLoadTime = (label, asyncFunction) => {
  return async (...args) => {
    const startTime = performance.now();

    try {
      const result = await asyncFunction(...args);
      const endTime = performance.now();
      const loadTime = endTime - startTime;

      console.log(`📊 ${label} loaded in ${loadTime.toFixed(2)}ms`);

      // Sparar i vår prestandabank för senare analys
      if (window.performanceMetrics) {
        window.performanceMetrics.push({
          label,
          loadTime,
          timestamp: Date.now(),
        });
      }

      return result;
    } catch (error) {
      const endTime = performance.now();
      const loadTime = endTime - startTime;
      console.error(
        `❌ ${label} failed after ${loadTime.toFixed(2)}ms:`,
        error
      );
      throw error;
    }
  };
};

/**
 * Debounce - som att vänta på att någon slutar skriva 🤐
 *
 * Perfekt för sökfält! Väntar tills användaren slutar skriva
 * innan vi faktiskt söker. Sparar massa onödiga anrop.
 */
export const debounce = (func, wait, immediate = false) => {
  let timeout;

  return function executedFunction(...args) {
    const later = () => {
      timeout = null;
      if (!immediate) func(...args);
    };

    const callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);

    if (callNow) func(...args);
  };
};

/**
 * Begränsar hur ofta en funktion körs 🚦
 *
 * Bra för scroll-events som annars spammar.
 */
export const throttle = (func, limit) => {
  let inThrottle;

  return function (...args) {
    if (!inThrottle) {
      func.apply(this, args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
};

/**
 * Mäter Firebase-frågornas hastighet 🔥
 */
export const measureFirebaseQuery = (queryName, queryFunction) => {
  return measureLoadTime(`Firebase Query: ${queryName}`, queryFunction);
};

/**
 * Kollar appens storlek 📦
 *
 * Visar vad som tar mest plats i appen.
 */
export const analyzeBundleSize = () => {
  if (process.env.NODE_ENV !== "development") {
    return;
  }

  // Uppskattad storlek för varje del
  const estimatedBundleSize = {
    react: 42, // KB
    firebase: 140, // KB (optimerat från 180KB)
    components: 95, // KB
    utils: 15, // KB
    styles: 25, // KB
  };

  const totalSize = Object.values(estimatedBundleSize).reduce(
    (sum, size) => sum + size,
    0
  );

  console.group("📦 Bundle Size Analysis");
  console.log(`Total estimated size: ${totalSize}KB`);

  Object.entries(estimatedBundleSize).forEach(([module, size]) => {
    const percentage = Math.round((size / totalSize) * 100);
    console.log(`${module}: ${size}KB (${percentage}%)`);
  });

  // Ger råd om appen blir för stor
  if (totalSize > 500) {
    console.warn("⚠️ Bundle size is large. Consider code splitting.");
  }

  if (estimatedBundleSize.firebase > 150) {
    console.warn(
      "⚠️ Firebase bundle is large. Consider importing only needed functions."
    );
    console.log("💡 Optimeringstips för Firebase:");
    console.log(
      "   • Importera bara använda funktioner från firebase/database och firebase/auth"
    );
    console.log("   • Använd tree-shaking genom specifika imports");
    console.log("   • Överväg lazy loading av Firebase-moduler");
    console.log(
      "   • Kontrollera att inga oanvända Firebase-tjänster importeras"
    );
  }

  console.groupEnd();
};

/**
 * Mäter Google:s viktiga prestanda-värden 🎯
 *
 * Kollar hur snabbt användarna upplever appen.
 */
export const observeWebVitals = () => {
  if (typeof PerformanceObserver === "undefined") {
    console.warn("PerformanceObserver not supported");
    return;
  }

  // LCP - tid innan viktigaste innehållet syns
  const lcpObserver = new PerformanceObserver((list) => {
    const entries = list.getEntries();
    const lastEntry = entries[entries.length - 1];
    console.log(`🎯 LCP: ${lastEntry.startTime.toFixed(2)}ms`);
  });

  try {
    lcpObserver.observe({ entryTypes: ["largest-contentful-paint"] });
  } catch (e) {
    console.warn("LCP observation not supported");
  }

  // FID - reaktionstid på första klick
  const fidObserver = new PerformanceObserver((list) => {
    const entries = list.getEntries();
    entries.forEach((entry) => {
      const fid = entry.processingStart - entry.startTime;
      console.log(`⚡ FID: ${fid.toFixed(2)}ms`);
    });
  });

  try {
    fidObserver.observe({ entryTypes: ["first-input"] });
  } catch (e) {
    console.warn("FID observation not supported");
  }
};

/**
 * Startar prestandaövervakning 🚀
 *
 * Avstängt för renare konsol.
 */
export const initPerformanceMonitoring = () => {
  // AVSTÄNGT: Performance monitoring för renare console
  // Aktivera genom att ändra till "development" nedan
  if (process.env.NODE_ENV !== "production_only") {
    return;
  }

  // Sparar alla mätningar här
  window.performanceMetrics = [];

  // Startar övervakning
  observeWebVitals();

  // Analyserar storlek när allt laddat
  setTimeout(analyzeBundleSize, 1000);

  // Kollar minne var 30:e sekund
  setInterval(() => {
    const memoryInfo = measureMemoryUsage();
    if (memoryInfo) {
      window.performanceMetrics.push({
        label: "Memory Usage",
        value: memoryInfo.usage,
        timestamp: Date.now(),
      });
    }
  }, 30000);

  console.log("🚀 Performance monitoring initialized");
};

/**
 * Exporterar alla prestandamätningar 📊
 *
 * Som att skriva en rapport över hur appen presterat.
 * Användbart för att analysera prestanda över tid.
 */
export const exportPerformanceMetrics = () => {
  if (!window.performanceMetrics) {
    console.warn("No performance metrics available");
    return null;
  }

  const metrics = {
    timestamp: Date.now(),
    userAgent: navigator.userAgent,
    metrics: window.performanceMetrics,
    summary: {
      totalMeasurements: window.performanceMetrics.length,
      averageLoadTime: window.performanceMetrics
        .filter((m) => m.loadTime)
        .reduce((sum, m, _, arr) => sum + m.loadTime / arr.length, 0),
    },
  };

  console.log("📊 Performance Metrics Export:", metrics);
  return metrics;
};
