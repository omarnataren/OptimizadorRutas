function abrirMapa() {
    if (typeof javaConnector !== "undefined") {
      javaConnector.abrirMapa();
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