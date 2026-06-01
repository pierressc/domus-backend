package com.domus.domus_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // CPF é sensível pela LGPD, mas obrigatório para o condomínio. 
    // unique = true garante que não existam dois cadastros iguais.
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha; // Será guardada criptografada posteriormente

    @Column(nullable = false)
    private String perfil; // N1, N2, N3, N4 ou N5

    @Column(nullable = false)
    private Boolean ativo = true; // Prepara o terreno para o Soft Delete futuro
}