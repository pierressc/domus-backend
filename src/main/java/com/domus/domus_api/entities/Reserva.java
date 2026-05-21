package com.domus.domus_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Área comum a ser reservada (ex: "Churrasqueira", "Salão de Festas")
    @Column(nullable = false)
    private String local;

    // A data exata da reserva
    @Column(nullable = false)
    private LocalDate dataReserva;

    // Horário que a reserva começa
    @Column(nullable = false)
    private LocalTime horaInicio;

    // Horário que a reserva termina
    @Column(nullable = false)
    private LocalTime horaFim;

    // Status: "PENDENTE", "APROVADA" ou "CANCELADA"
    @Column(nullable = false)
    private String status = "PENDENTE"; // Já definimos o padrão inicial exigido no card!

    // Vincula a reserva ao morador que a solicitou
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario morador;
}