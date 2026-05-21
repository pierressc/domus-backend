package com.domus.domus_api.repositories;

import com.domus.domus_api.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // A mágica matemática do RF06 para verificar sobreposição de horários
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.local = :local " +
           "AND r.dataReserva = :data AND r.status = 'APROVADA' " +
           "AND r.horaInicio < :horaFim AND r.horaFim > :horaInicio")
    boolean existeConflitoDeHorario(
            @Param("local") String local, 
            @Param("data") LocalDate data, 
            @Param("horaInicio") LocalTime horaInicio, 
            @Param("horaFim") LocalTime horaFim
    );
}