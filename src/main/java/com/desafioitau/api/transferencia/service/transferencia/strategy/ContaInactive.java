package com.desafioitau.api.transferencia.service.transferencia.strategy;

import com.desafioitau.api.transferencia.config.exception.conta.ContaInactiveException;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.TransferenciaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContaInactive implements CheckTransferenciaContasInterface {
    @Override
    public void checkTransferenciaContas(TransferenciaEntity transferencia, ContaEntity contaOrigem) {
        if (!contaOrigem.isAtivo()) {
            throw new ContaInactiveException(transferencia.getConta().getIdOrigem());
        }
    }
}
