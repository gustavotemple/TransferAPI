package com.desafioitau.api.transferencia.config.exception.conta;

public class ContaDailyLimitException extends ContaException {

    public ContaDailyLimitException(String id, double limite) {
        super(String.format("Ultrapassou o limite diario (%.2f) da conta %s.", limite, id));
    }

}
