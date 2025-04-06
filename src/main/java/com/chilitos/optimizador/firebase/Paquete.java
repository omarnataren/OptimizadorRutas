package com.chilitos.optimizador.firebase;

public class Paquete {
    private String nombre ;
    private String descripcion;
    private double peso;
    private String direccion;
    private String estatus;

    public Paquete() {
        
    }

    public Paquete(String nombre, String descripcion, double peso, String direccion, String estatus) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.peso = peso;
        this.direccion = direccion;
        this.estatus = estatus;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    
}
