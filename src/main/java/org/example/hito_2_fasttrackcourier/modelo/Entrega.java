package org.example.hito_2_fasttrackcourier.modelo;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEntrega;
    private String domicilio;
    private String dni_cliente;
    private String nombreCliente;

    @Enumerated(EnumType.STRING)
    private EstadoEntrega estado;
    private String dniAdministrador;
    private String dniConductor;
    private Timestamp fechaHoraRegistro;
    private Timestamp fechaHoraSalida;
    private Date fechaEntregaPrevista;
    private Timestamp fechaHoraEntrega;


    public Entrega(String domicilio, String dni_cliente, String nombreCliente, EstadoEntrega estado, String dniAdministrador, String dniConductor, Timestamp fechaHoraRegistro, Timestamp fechaHoraSalida, Date fechaEntregaPrevista, Timestamp fechaHoraEntrega) {
        this.domicilio = domicilio;
        this.dni_cliente = dni_cliente;
        this.nombreCliente = nombreCliente;
        this.estado = estado;
        this.dniAdministrador = dniAdministrador;
        this.dniConductor = dniConductor;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaEntregaPrevista = fechaEntregaPrevista;
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public Entrega() {

    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public Timestamp getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Timestamp fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Timestamp getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Timestamp fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public String getDniConductor() {
        return dniConductor;
    }

    public void setDniConductor(String dniConductor) {
        this.dniConductor = dniConductor;
    }

    public Timestamp getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(Timestamp fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public Date getFechaEntregaPrevista() {
        return fechaEntregaPrevista;
    }

    public void setFechaEntregaPrevista(Date fechaEntregaPrevista) {
        this.fechaEntregaPrevista = fechaEntregaPrevista;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDniAdministrador() {
        return dniAdministrador;
    }

    public void setDniAdministrador(String dniAdministrador) {
        this.dniAdministrador = dniAdministrador;
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
