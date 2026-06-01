package com.domus.domus_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate data = LocalDate.now();

    @Column(nullable = false)
    private String status = "ABERTA";

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario morador;
}