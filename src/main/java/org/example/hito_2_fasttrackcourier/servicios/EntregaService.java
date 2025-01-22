package org.example.hito_2_fasttrackcourier.servicios;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.example.hito_2_fasttrackcourier.repositories.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Entrega> getAllEntregas() {
        return (List<Entrega>) entregaRepository.findAll();
    }

    public List<Entrega> getEntregasPorConductor(String dniConductor) {
        return entregaRepository.findByDniConductor(dniConductor);
    }

    public void crearEntrega(String domicilio, String dniCliente, String nombreCliente, String dniConductor) {
        Entrega entrega = new Entrega();
        entrega.setDomicilio(domicilio);
        entrega.setDni_cliente(dniCliente);
        entrega.setNombreCliente(nombreCliente);
        entrega.setDniConductor(dniConductor);
        entrega.setEstado(Entrega.EstadoEntrega.RECIBIDO);
        entrega.setFechaHoraRegistro(Timestamp.valueOf(LocalDateTime.now()));
        entregaRepository.save(entrega);
    }

    public void actualizarEstado(int idEntrega, String estado, String fechaHoraEntrega) {
        Entrega entrega = entregaRepository.findById(idEntrega).orElseThrow(() -> new IllegalArgumentException("Entrega no encontrada"));
        entrega.setEstado(Entrega.EstadoEntrega.valueOf(estado.toUpperCase()));
        if (estado.equalsIgnoreCase("entregado") && fechaHoraEntrega != null) {
            entrega.setFechaHoraEntrega(Timestamp.valueOf(LocalDateTime.parse(fechaHoraEntrega)));
        }
        entregaRepository.save(entrega);
    }

    public long getTotalEntregas() {
        return entregaRepository.count();
    }

    public long getEntregasPorEstado(Entrega.EstadoEntrega estado) {
        return entregaRepository.countByEstado(estado);
    }

    public List<Object[]> getEntregasPorConductor() {
        return entregaRepository.countByConductor();
    }
}