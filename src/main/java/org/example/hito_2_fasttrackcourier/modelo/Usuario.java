package org.example.hito_2_fasttrackcourier.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class Usuario {

    @Id
    private String dni;
    private String nombre;
    private String rol;
    private String password;
    private boolean activo;
    private String telefono;
    private String email;

    public Usuario(String dni, String nombre, String rol, String password, boolean activo, String telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.rol = rol;
        this.password = password;
        this.activo = activo;
        this.telefono = telefono;
        this.email = email;
    }

    public Usuario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
