package com.desafioitau.api.transferencia.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaldoRequestDTO {

    private double valor;
    private String nomeDestino;
    private Conta conta;

    @Getter
    @Setter
    @ToString
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}
