package com.desafioitau.api.transferencia.service.transferencia.strategy;

import com.desafioitau.api.transferencia.config.exception.conta.ContaDailyLimitException;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.TransferenciaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContaDailyLimit implements CheckTransferenciaContasInterface {

    private static final int MIN_LIMIT = 0;

    @Override
    public void checkTransferenciaContas(TransferenciaEntity transferencia, ContaEntity contaOrigem)  {
        if (contaOrigem.getLimiteDiario() <= MIN_LIMIT || contaOrigem.getLimiteDiario() < transferencia.getValor()) {
            throw new ContaDailyLimitException(transferencia.getConta().getIdOrigem(), contaOrigem.getLimiteDiario());
        }
    }
}
