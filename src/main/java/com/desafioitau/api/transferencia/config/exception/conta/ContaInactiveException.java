package com.desafioitau.api.transferencia.config.exception.conta;

public class ContaInactiveException extends ContaException {

    public ContaInactiveException(String id) {
        super(String.format("Conta %s inativa.", id));
    }

}
