package org.example.hito_2_fasttrackcourier.servicios;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.example.hito_2_fasttrackcourier.repositories.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    @Autowired
    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    // Total de entregas
    public long countTotalEntregas() {
        return entregaRepository.count();
    }

    // Entregas por estado
    public long countByEstado(Entrega.EstadoEntrega estado) {
        return entregaRepository.countByEstado(estado);
    }

    // Entregas por conductor
    public Map<String, Long> countEntregasPorConductor() {
        List<Object[]> resultados = entregaRepository.countEntregasPorConductor();
        Map<String, Long> conteo = new LinkedHashMap<>();

        for (Object[] resultado : resultados) {
            String dni = (String) resultado[0];
            Long cantidad = (Long) resultado[1];
            conteo.put(dni != null ? dni : "Sin asignar", cantidad);
        }

        return conteo;
    }
}