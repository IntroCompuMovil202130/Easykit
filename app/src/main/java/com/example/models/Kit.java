package com.example.models;

import android.graphics.drawable.Drawable;

import java.util.Objects;

public class Kit {
    private int id;
    private String nombre;
    private String descripcion;
    private Drawable imagen;
    private int precio;

    public Kit() {
    }

    public Kit(int id, String nombre, String descripcion, Drawable imagen, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Drawable getImagen() {
        return this.imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public int getPrecio() {
        return this.precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Kit id(int id) {
        setId(id);
        return this;
    }

    public Kit nombre(String nombre) {
        setNombre(nombre);
        return this;
    }

    public Kit descripcion(String descripcion) {
        setDescripcion(descripcion);
        return this;
    }

    public Kit imagen(Drawable imagen) {
        setImagen(imagen);
        return this;
    }

    public Kit precio(int precio) {
        setPrecio(precio);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Kit)) {
            return false;
        }
        Kit kit = (Kit) o;
        return id == kit.id && Objects.equals(nombre, kit.nombre) && Objects.equals(descripcion, kit.descripcion) && Objects.equals(imagen, kit.imagen) && precio == kit.precio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, imagen, precio);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", precio='" + getPrecio() + "'" +
            "}";
    }

}
