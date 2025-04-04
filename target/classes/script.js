function abrirMapa() {
    if (typeof javaConnector !== "undefined") {
        alert("HOLA");
      javaConnector.abrirMapa();
      alert("HOLA BB");
    } else {
      alert("Conexión con Java no disponible");
    }
}
const botonMapa = document.getElementById("botonMapa");
botonMapa.addEventListener("click", abrirMapa);

function mostrarPaquetes(paquetes){
    const tabla = document.getElementById("tablaPaquetes").getElementsByTagName("tbody")[0];
    tabla.innerHTML = ""; 

    paquetes.forEach((p, i) => {
        const fila = tabla.insertRow();
        fila.innerHTML = `
            <td>${i + 1}</td>
            <td>${p.descripcion}</td>
            <td>${p.peso} kg</td>
            <td>${p.direccion}</td>
            <td>${p.estatus}</td>
        `;
    });
}

function mostrarTransportista(transportista){
    const tabla = document.getElementById("tablaTransportistas").getElementsByTagName("tbody")[0];
    tabla.innerHTML = ""; 

    paquetes.forEach((t) => {
        const fila = tabla.insertRow();
        fila.innerHTML = `
            <td>${t.id}</td>
            <td>${t.nombre} kg</td>
            <td>${t.apellidoP}</td>
            <td>${t.apellidoM}</td>
        `;
    });
}

setTimeout(() => {
    if (typeof javaConnector !== "undefined") {
      const paquetesJson = javaConnector.obtenerPaquetesComoJson();
      const paquetes = JSON.parse(paquetesJson);
      mostrarPaquetes(paquetes);
    }
  }, 1000);

  