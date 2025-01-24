package org.example.hito_2_fasttrackcourier.servicios;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.example.hito_2_fasttrackcourier.repositories.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public Map<String, Object> obtenerMetricasConductor(String dniConductor) {
        List<Entrega> entregas = entregaRepository.findByDniConductor(dniConductor);
        Map<String, Object> metricas = new LinkedHashMap<>();

        long totalEntregas = entregas.size();
        long entregasExitosas = entregas.stream()
                .filter(e -> e.getEstado() == Entrega.EstadoEntrega.entregado)
                .count();
        double porcentajeExito = totalEntregas > 0 ? (entregasExitosas * 100.0) / totalEntregas : 0.0;
        metricas.put("porcentajeExito", Math.round(porcentajeExito));

        long entregasATiempo = entregas.stream()
                .filter(e -> e.getEstado() == Entrega.EstadoEntrega.entregado)
                .filter(e -> e.getFechaHoraEntrega() != null && e.getFechaEntregaPrevista() != null
                        && e.getFechaHoraEntrega().isBefore(e.getFechaEntregaPrevista().atStartOfDay()))
                .count();
        double porcentajeEntregasATiempo = totalEntregas > 0 ? (entregasATiempo * 100.0) / totalEntregas : 0.0;
        metricas.put("porcentajeEntregasATiempo", Math.round(porcentajeEntregasATiempo));

        List<Entrega> entregasCompletadas = entregas.stream()
                .filter(e -> e.getEstado() == Entrega.EstadoEntrega.entregado)
                .filter(e -> e.getFechaHoraSalida() != null && e.getFechaHoraEntrega() != null)
                .sorted(Comparator.comparing(Entrega::getFechaHoraRegistro).reversed())
                .limit(5)
                .collect(Collectors.toList());

        metricas.put("ultimasEntregas", entregasCompletadas);

        if (!entregasCompletadas.isEmpty()) {
            long tiempoTotal = entregasCompletadas.stream()
                    .mapToLong(e -> Duration.between(e.getFechaHoraSalida(), e.getFechaHoraEntrega()).toMillis())
                    .sum();
            long promedioMillis = tiempoTotal / entregasCompletadas.size();
            long minutos = TimeUnit.MILLISECONDS.toMinutes(promedioMillis);
            metricas.put("tiempoPromedio", minutos);
        } else {
            metricas.put("tiempoPromedio", 0L);
        }

        long entregasProgramadas = totalEntregas;
        long entregasRealizadas = entregasExitosas;
        metricas.put("entregasProgramadas", entregasProgramadas);
        metricas.put("entregasRealizadas", entregasRealizadas);

        LocalDate ahora = LocalDate.now();
        LocalDate haceUnaSemana = ahora.minusWeeks(1);
        LocalDate haceUnMes = ahora.minusMonths(1);

        long entregasUltimaSemana = entregas.stream()
                .filter(e -> e.getFechaHoraRegistro().toLocalDate().isAfter(haceUnaSemana))
                .count();
        long entregasUltimoMes = entregas.stream()
                .filter(e -> e.getFechaHoraRegistro().toLocalDate().isAfter(haceUnMes))
                .count();
        metricas.put("entregasUltimaSemana", entregasUltimaSemana);
        metricas.put("entregasUltimoMes", entregasUltimoMes);

        // Filtrar solo entregas completadas para el gráfico
        metricas.put("ultimasEntregas", entregasCompletadas.stream()
                .filter(e -> e.getFechaHoraSalida() != null && e.getFechaHoraEntrega() != null) // Asegurar que no sean nulas
                .sorted(Comparator.comparing(Entrega::getFechaHoraRegistro).reversed())
                .limit(5)
                .collect(Collectors.toList()));

        return metricas;
    }
}