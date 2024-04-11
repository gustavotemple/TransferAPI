package com.desafioitau.api.transferencia.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ContaEntity {

    private String id;
    private double saldo;
    private double limiteDiario;
    private boolean ativo;
}
