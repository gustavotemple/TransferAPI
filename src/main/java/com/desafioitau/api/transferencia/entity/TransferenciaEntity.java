package com.desafioitau.api.transferencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaEntity {

    private String idCliente;
    private double valor;
    private Conta conta;

    @Getter
    @Setter
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}
