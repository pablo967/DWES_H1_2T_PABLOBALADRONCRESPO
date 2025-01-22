package org.example.hito_2_fasttrackcourier.repositories;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntregaRepository extends CrudRepository<Entrega, Integer> {
    List<Entrega> findByDniConductor(String dniConductor);

    int countByEstado(Entrega.EstadoEntrega estado);

    @Query("SELECT e.dniConductor, COUNT(e) FROM Entrega e GROUP BY e.dniConductor")
    List<Object[]> countByConductor();
}