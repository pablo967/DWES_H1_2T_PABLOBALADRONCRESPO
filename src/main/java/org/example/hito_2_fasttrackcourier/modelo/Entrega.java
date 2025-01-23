package org.example.hito_2_fasttrackcourier.modelo;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime fechaHoraRegistro;
    private LocalDateTime fechaHoraSalida;
    private LocalDate fechaEntregaPrevista;
    private LocalDateTime fechaHoraEntrega;


    

    public Entrega() {

    }


    public Entrega(String domicilio, String dni_cliente, String nombreCliente, EstadoEntrega estado,
                   String dniAdministrador, String dniConductor, LocalDateTime fechaHoraRegistro,
                   LocalDateTime fechaHoraSalida, LocalDate fechaEntregaPrevista, LocalDateTime fechaHoraEntrega) {

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

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }


    public String getDniConductor() {
        return dniConductor;
    }

    public void setDniConductor(String dniConductor) {
        this.dniConductor = dniConductor;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public LocalDate getFechaEntregaPrevista() {
        return fechaEntregaPrevista;
    }

    public void setFechaEntregaPrevista(LocalDate fechaEntregaPrevista) {
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
        recibido, preparado, en_camino, entregado
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }
}
