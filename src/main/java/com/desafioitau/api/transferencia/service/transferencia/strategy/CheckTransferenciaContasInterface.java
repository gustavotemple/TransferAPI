package com.desafioitau.api.transferencia.service.transferencia.strategy;

import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.TransferenciaEntity;

public interface CheckTransferenciaContasInterface {

    void checkTransferenciaContas(TransferenciaEntity transferencia, ContaEntity contaOrigem);

}
