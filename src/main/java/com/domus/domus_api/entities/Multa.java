package com.domus.domus_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "multas")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDate dataAplicacao = LocalDate.now();

    // Vincula a multa ao morador infrator
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario infrator;

    // Vincula opcionalmente a multa a uma ocorrência prévia
    @ManyToOne
    @JoinColumn(name = "ocorrencia_id")
    private Ocorrencia ocorrencia;
}