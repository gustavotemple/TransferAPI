package com.desafioitau.api.transferencia.service.transferencia.strategy;

import com.desafioitau.api.transferencia.config.exception.conta.ContaSaldoInsufficientException;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.TransferenciaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContaSaldoInsufficient implements CheckTransferenciaContasInterface {
    @Override
    public void checkTransferenciaContas(TransferenciaEntity transferencia, ContaEntity contaOrigem) {
        if (contaOrigem.getSaldo() < transferencia.getValor()) {
            throw new ContaSaldoInsufficientException(transferencia.getConta().getIdOrigem(), contaOrigem.getSaldo());
        }
    }
}
