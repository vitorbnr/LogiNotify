package com.vitorbnr.LogiNotify.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_entregas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDestinatario;

    private String endereco;

    private String emailDestinatario;

    @Enumerated(EnumType.STRING)
    private StatusEntrega status;

}
