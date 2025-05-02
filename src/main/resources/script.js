// Elementos referenciados
const botonMapa = document.getElementById("botonMapa");
const navPaquetes = document.getElementById('navPaquetes')
const navTransportistas = document.getElementById('navTransportistas')
const abrirFormP = document.getElementById('abrirFormularioPaquetes')
const abrirFormT = document.getElementById('abrirFormularioTransportistas')
const botonEliminarPaquete = document.getElementById("eliminarPaquete")
const botonEliminarTransportista = document.getElementById("eliminarTransportista")
const formPaquetes = document.getElementById("formPaquete")
const formTransportista = document.getElementById("formTransportista")

let paqueteSeleccionados = [];
let paquetesParaEliminar = [];
let transportistaSeleccionado = null;

// EVENTOS NAV Y FORMULARIOS
navTransportistas.onclick = () => mostrar('Transportistas')
navPaquetes.onclick = () => mostrar('Paquetes')
abrirFormP.onclick = () => abrirModal('FormularioPaquetes')
abrirFormT.onclick = () => abrirModal('FormularioTransportistas')
botonEliminarTransportista.onclick = () => eliminarTransportista()
botonEliminarPaquete.onclick = () => eliminarPaquetes()


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
            <td><input type="checkbox" class="check-paquete" data-direccion="${p.direccion}" data-id="${p.id}" id="${p.id}" /></td>
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
            <td><input type="radio" name="transportista" class="radio-transportista" value="${t.id}" id="${t.id}" /></td>
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

// SELECCIONAR PAQUETES Y TRANS
document.addEventListener("change", (e) => {
    if (e.target.classList.contains("check-paquete")) {
      const direccion = e.target.dataset.direccion;
      const id = e.target.dataset.id;
  
      if (e.target.checked) {
        document.getElementById(id);
        paqueteSeleccionados.push(direccion);
        paquetesParaEliminar.push(id)
        alert(paquetesParaEliminar);
        alert("Se añadio un paquete: " + paqueteSeleccionados);
      } else {
        paqueteSeleccionados = paqueteSeleccionados.filter(d => d !== direccion);
        paquetesParaEliminar = paquetesParaEliminar.filter(i => i !== id);
      }
  
      verificarActivacionBotonMapa();
      verificarBotonesEliminar();
    }
  
    if (e.target.classList.contains("radio-transportista")) {
      transportistaSeleccionado = e.target.value;
      alert("Se añadio un transportista: " + transportistaSeleccionado);
      verificarActivacionBotonMapa();
      verificarBotonesEliminar();
    }
});

// VERIFICAR BTON
function verificarActivacionBotonMapa() {
    botonMapa.disabled = !(transportistaSeleccionado && paqueteSeleccionados.length > 0);
}
//VERIFICAR BTN ELIMINAR 
function verificarBotonesEliminar() {
    botonEliminarTransportista.disabled = !(transportistaSeleccionado);
    botonEliminarPaquete.disabled = !(paqueteSeleccionados.length > 0);
}

// ABRIR MAPA
botonMapa.addEventListener("click", function abrirMapa() {
    if (typeof javaConnector !== "undefined") {
        javaConnector.setDirecciones(paqueteSeleccionados);
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
    const estatus = formData.get("Estatus")

    if (!nombre || !descripcion || isNaN(peso) || !direccion || !estatus) {
        alert("Por favor completa todos los campos correctamente.");
        return;
    }

    javaConnector.añadirPaquetes(nombre, descripcion, peso, direccion,estatus );
    const nuevosPaquetes = JSON.parse(javaConnector.obtenerPaquetesComoJson());
    mostrarPaquetes(nuevosPaquetes);

    cerrarModal('FormularioPaquetes');
    this.reset();
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
    this.reset();
});

//ELIMINAR DATOS DE TRANSPORTISTAS Y DE PAQUETES
function eliminarPaquetes(){
    alert("seleccionaste eliminar");
    paquetesParaEliminar.forEach(paquete => {
        alert(paquete)
        javaConnector.eliminarPaquetes("paquetes", paquete);
    })
    paquetesParaEliminar = [];
    paqueteSeleccionados = [];
    const nuevos = JSON.parse(javaConnector.obtenerPaquetesComoJson());
    mostrarPaquetes(nuevos);
}

function eliminarTransportista(){
    alert("seleccionaste eliminar");
    javaConnector.eliminarPaquetes("transportista", transportistaSeleccionado)
    transportistaSeleccionado = null;
    const nuevos = JSON.parse(javaConnector.obtenerTransportistasComoJson());
    mostrarTransportistas(nuevos);
}

//CARGAR LOS DATOS
cargarDatos()