package com.desafioitau.api.transferencia.config.exception.conta;

public class ContaSaldoInsufficientException extends ContaException {

    public ContaSaldoInsufficientException(String id, double saldo) {
        super(String.format("Saldo insuficiente (%.2f) da conta %s.", saldo, id));
    }

}
