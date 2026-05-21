package com.domus.domus_api.controllers;

import com.domus.domus_api.entities.Reserva;
import com.domus.domus_api.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    // RF05: Morador solicita uma nova reserva
    @PostMapping
    public ResponseEntity<Reserva> solicitarReserva(@RequestBody Reserva novaReserva) {
        
        // Como você definiu private String status = "PENDENTE" lá na entidade,
        // não precisamos setar o status aqui. O Java já sabe o que fazer!
        
        Reserva reservaSalva = reservaRepository.save(novaReserva);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaSalva);
    }

    // RF06: Síndico atualiza a reserva (Aprovar ou Cancelar)
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva não encontrada.");
        }

        Reserva reserva = reservaOpt.get();

        // Blinda a API caso o frontend não envie o campo "status"
        if (!body.containsKey("status") || body.get("status") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo 'status' é obrigatório.");
        }

        String novoStatus = body.get("status").toUpperCase();

        // Se o síndico quiser apenas cancelar, liberamos direto
        if (novoStatus.equals("CANCELADA")) {
            reserva.setStatus(novoStatus);
            reservaRepository.save(reserva);
            return ResponseEntity.ok(reserva);
        }

        // Se o síndico quiser aprovar, rodamos a regra de negócio de conflito
        if (novoStatus.equals("APROVADA")) {
            boolean temConflito = reservaRepository.existeConflitoDeHorario(
                    reserva.getLocal(),
                    reserva.getDataReserva(),
                    reserva.getHoraInicio(),
                    reserva.getHoraFim()
            );

            if (temConflito) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Erro: Já existe uma reserva aprovada para este local que conflita com este horário.");
            }

            reserva.setStatus(novoStatus);
            reservaRepository.save(reserva);
            return ResponseEntity.ok(reserva);
        }

        // Se mandarem um status que não existe
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido.");
    }
}