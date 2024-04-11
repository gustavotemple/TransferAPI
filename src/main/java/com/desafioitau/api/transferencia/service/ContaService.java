package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaNotFoundException;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.SaldoEntity;
import com.desafioitau.api.transferencia.repository.ContasRepository;
import com.desafioitau.api.transferencia.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.util.Optional;

@Service
public class ContaService {

    private final ContasRepository contasRepository;

    private final SaldoRepository saldoRepository;

    @Autowired
    public ContaService(ContasRepository contasRepository,
                        SaldoRepository saldoRepository) {
        this.contasRepository = contasRepository;
        this.saldoRepository = saldoRepository;
    }

    public ContaEntity getContaById(String idConta) {
        Optional<ContaEntity> conta;
        try {
            conta = contasRepository.findById(idConta);
        } catch (Exception e) {
            Logger.error(e);
            throw new ServiceUnavailableException(ContaService.class.getName());
        }

        if (conta.isEmpty()) {
            throw new ContaNotFoundException(idConta);
        }

        return conta.get();
    }

    public void updateSaldoConta(SaldoEntity saldo) {
        try {
            saldoRepository.update(saldo);
        } catch (Exception e) {
            Logger.error(e);
            throw new ServiceUnavailableException(ContaService.class.getName());
        }
    }

}
