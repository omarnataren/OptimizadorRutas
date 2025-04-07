const map = L.map('map').setView([17.054, -96.725], 13);
let marcadoresOrigen = [];
let marcadoresDestino = [];

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
}).addTo(map);

// map.on('click', async function (e) {
//     const { lat, lng } = e.latlng;

//     if (typeof javaConnector !== "undefined") {
//         const modo = javaConnector.getModoSeleccion();

//         if (modo === "origen" || modo === "destino") {
//             javaConnector.setPoint(lat, lng);

//             const marker = L.marker([lat, lng], {
//                 title: modo
//             }).addTo(map).bindPopup(`${modo} (seleccionado manualmente)`).openPopup();

//             if (modo === "origen") {
//                 marcadoresOrigen.forEach(m => map.removeLayer(m));
//                 marcadoresOrigen = [marker];
//             } else if (modo === "destino") {
//                 marcadoresDestino.push(marker);
//             }
//         }
//     }
// });

function buscarLugar(query, tipo) {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}`;
    fetch(url)
        .then(res => res.json())
        .then(data => {
            if (data.length > 0) {
            const lat = parseFloat(data[0].lat);
            const lon = parseFloat(data[0].lon);
            const marker = L.marker([lat, lon]).addTo(map).bindPopup(`${tipo}: ${query}`).openPopup();
            map.setView([lat, lon], 15);

            if (tipo === "origen") {
                marcadoresOrigen.forEach(m => map.removeLayer(m));
                marcadoresOrigen = [marker];
            } else if (tipo === "destino") {
                alert("Se añadira un nuevo destino")
                marcadoresDestino.push(marker);
            }

            if (typeof javaConnector !== "undefined") {
                javaConnector.setModoSeleccion(tipo);
                javaConnector.setPoint(lat, lon);
            }
            } else {
                alert("Lugar no encontrado");
            }
    });
}

    function limpiarMarcadores(tipo) {
    if (tipo === "destino") {
        marcadoresDestino.forEach(m => map.removeLayer(m));
        marcadoresDestino = [];
    } else if (tipo === "origen") {
        marcadoresOrigen.forEach(m => map.removeLayer(m));
        marcadoresOrigen = [];
    }
}