package com.domus.domus_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unidades")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String bloco;

    @Column(nullable = false, length = 10)
    private String numero;

    // Aqui é onde vinculamos a unidade ao morador
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario morador;
}