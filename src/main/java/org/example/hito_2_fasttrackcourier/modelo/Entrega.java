package org.example.hito_2_fasttrackcourier.modelo;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_entrega;
    private String domicilio;
    private String dni_cliente;
    private String nombre_cliente;
    private EstadoEntrega estado;
    private String dni_administrador;
    private String dni_conductor;
    private Timestamp fecha_hora_registro;
    private Timestamp fecha_hora_salida;
    private Date fecha_entrega_prevista;
    private Timestamp fecha_hora_entrega;


    public Entrega(String domicilio, String dni_cliente, String nombre_cliente, EstadoEntrega estado, String dni_administrador, String dni_conductor, Timestamp fecha_hora_registro, Timestamp fecha_hora_salida, Date fecha_entrega_prevista, Timestamp fecha_hora_entrega) {
        this.domicilio = domicilio;
        this.dni_cliente = dni_cliente;
        this.nombre_cliente = nombre_cliente;
        this.estado = estado;
        this.dni_administrador = dni_administrador;
        this.dni_conductor = dni_conductor;
        this.fecha_hora_registro = fecha_hora_registro;
        this.fecha_hora_salida = fecha_hora_salida;
        this.fecha_entrega_prevista = fecha_entrega_prevista;
        this.fecha_hora_entrega = fecha_hora_entrega;
    }

    public Entrega() {

    }

    public int getId_entrega() {
        return id_entrega;
    }

    public void setId_entrega(int id_entrega) {
        this.id_entrega = id_entrega;
    }

    public Timestamp getFecha_hora_salida() {
        return fecha_hora_salida;
    }

    public void setFecha_hora_salida(Timestamp fecha_hora_salida) {
        this.fecha_hora_salida = fecha_hora_salida;
    }

    public Timestamp getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(Timestamp fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }

    public String getDni_conductor() {
        return dni_conductor;
    }

    public void setDni_conductor(String dni_conductor) {
        this.dni_conductor = dni_conductor;
    }

    public Timestamp getFecha_hora_entrega() {
        return fecha_hora_entrega;
    }

    public void setFecha_hora_entrega(Timestamp fecha_hora_entrega) {
        this.fecha_hora_entrega = fecha_hora_entrega;
    }

    public Date getFecha_entrega_prevista() {
        return fecha_entrega_prevista;
    }

    public void setFecha_entrega_prevista(Date fecha_entrega_prevista) {
        this.fecha_entrega_prevista = fecha_entrega_prevista;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getDni_administrador() {
        return dni_administrador;
    }

    public void setDni_administrador(String dni_administrador) {
        this.dni_administrador = dni_administrador;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrega estado) {
        this.estado = estado;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public enum EstadoEntrega {
        RECIBIDO, PREPARADO, EN_CAMINO, ENTREGADO
    }
}
