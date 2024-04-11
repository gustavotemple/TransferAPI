package com.desafioitau.api.transferencia.config.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String service) {
        super(String.format("Servico %s indisponivel.", service));
    }

}
