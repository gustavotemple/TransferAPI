package com.desafioitau.api.transferencia.config.exception.conta;

public class ContaNotFoundException extends ContaException {

    public ContaNotFoundException(String id) {
        super(String.format("Conta %s nao encontrada.", id));
    }

}
