// Elementos referenciados
const botonMapa = document.getElementById("botonMapa");
const navPaquetes = document.getElementById('navPaquetes')
const navTransportistas = document.getElementById('navTransportistas')
const abrirFormP = document.getElementById('abrirFormularioPaquetes')
const abrirFormT = document.getElementById('abrirFormularioTransportistas')
const formPaquetes = document.getElementById("formPaquete")
const formTransportista = document.getElementById("formTransportista")

// EVENTOS NAV Y FORMULARIOS
navTransportistas.onclick = () => mostrar('Transportistas')
navPaquetes.onclick = () => mostrar('Paquetes')
abrirFormP.onclick = () => abrirModal('FormularioPaquetes')
abrirFormT.onclick = () => abrirModal('FormularioTransportistas')

// MOSTRAR SECCIONES DEL NAVV
function mostrar(id) {
    document.getElementById('Paquetes').style.display = 'none';
    document.getElementById('Transportistas').style.display = 'none';
    cerrarModal('FormularioPaquetes')
    cerrarModal('FormularioTransportistas')
    document.getElementById(id).style.display = 'block';
}

// MOSTRAR PAQUETES Y TRANSPORTISTAS
function mostrarPaquetes(paquetes){
    const tabla = document.getElementById("tablaPaquetes").getElementsByTagName("tbody")[0];
    tabla.innerHTML = ""; 

    paquetes.forEach((p) => {
        const fila = tabla.insertRow();
        fila.innerHTML = `
            <td>${p.id}</td>
            <td>${p.nombre}</td>
            <td>${p.descripcion}</td>
            <td>${p.peso} kg</td>
            <td>${p.direccion}</td>
            <td>${p.estatus}</td>
        `;
    });
}
function mostrarTransportistas(transportista){
    const tabla = document.getElementById("tablaTransportistas").getElementsByTagName("tbody")[0];
    tabla.innerHTML = ""; 

    transportista.forEach((t) => {
        const fila = tabla.insertRow();
        fila.innerHTML = `
            <td>${t.id}</td>
            <td>${t.nombre}</td>
            <td>${t.apellidoP}</td>
            <td>${t.apellidoM}</td>
        `;
    });
}


//FUNCIONES PARA ABRIR Y CERRAR LOS FORMS
function abrirModal(id) {
    document.getElementById(id).style.display = "flex";
 }
  
function cerrarModal(id) {
    document.getElementById(id).style.display = "none";
}

// CARGAR DATOS AL INICIO
function cargarDatos() {
    setTimeout(() => {
        if (typeof javaConnector !== "undefined") {
          const paquetesJson = javaConnector.obtenerPaquetesComoJson();
          const paquetes = JSON.parse(paquetesJson);
          mostrarPaquetes(paquetes);
    
          const transportistasJson = javaConnector.obtenerTransportistasComoJson();
          const transportistas = JSON.parse(transportistasJson);
          mostrarTransportistas(transportistas);
        }
      }, 50);
}

// ABRIR MAPA
botonMapa.addEventListener("click", function abrirMapa() {
    if (typeof javaConnector !== "undefined") {
      javaConnector.abrirMapa();
      alert("Mapa abierto");
    } else {
      alert("Conexión con Java no disponible");
    }
});
//SUBMIT DE PAQUETES
formPaquetes.addEventListener("submit", function(e) {
    e.preventDefault();
    
    const formData = new FormData(this);
    const nombre = formData.get("Nombre")
    const descripcion = formData.get("Descripcion")
    const peso = parseFloat(formData.get("Peso"))
    const direccion = formData.get("Direccion")

    if (!nombre || !descripcion || isNaN(peso) || !direccion) {
        alert("Por favor completa todos los campos correctamente.");
        return;
    }

    javaConnector.añadirPaquetes(nombre, descripcion, peso, direccion,"pendiente" );
    const nuevosPaquetes = JSON.parse(javaConnector.obtenerPaquetesComoJson());
    mostrarPaquetes(nuevosPaquetes);

    cerrarModal('FormularioPaquetes');
});
//SUBMIT DE TRANSPORTISTAS
formTransportista.addEventListener("submit", function(e) {
    e.preventDefault();
    
    const formData = new FormData(this);
    const nombre = formData.get("Nombre")
    const apellidoP = formData.get("Apellido Paterno")
    const apellidoM = formData.get("Apellido Materno")

    if (!nombre || !apellidoP || !apellidoM) {
        alert("Por favor completa todos los campos correctamente.");
        return;
    }

    javaConnector.añadirTransportistas(nombre, apellidoP, apellidoM);
    const nuevosTransportistas = JSON.parse(javaConnector.obtenerTransportistasComoJson());
    mostrarTransportistas(nuevosTransportistas);

    cerrarModal('FormularioTransportistas');
});

//CARGAR LOS DATOS
cargarDatos()