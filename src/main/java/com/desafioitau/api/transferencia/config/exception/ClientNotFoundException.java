package com.desafioitau.api.transferencia.config.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String id) {
        super(String.format("Cadastro do cliente com o id %s nao encontrado.", id));
    }

}
