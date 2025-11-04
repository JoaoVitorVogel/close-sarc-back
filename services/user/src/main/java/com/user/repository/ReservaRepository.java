package com.user.repository;

import com.user.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.recurso.id = :recursoId " +
           "AND r.status = 'ATIVA' " +
           "AND r.evento.dataInicio < :dataFim " +
           "AND r.evento.dataFim > :dataInicio")
    List<Reserva> findReservasConflitantes(
            @Param("recursoId") Long recursoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
