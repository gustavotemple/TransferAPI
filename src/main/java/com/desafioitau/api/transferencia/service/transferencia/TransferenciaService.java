package com.desafioitau.api.transferencia.service.transferencia;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.entity.*;
import com.desafioitau.api.transferencia.service.BacenService;
import com.desafioitau.api.transferencia.service.CadastroClientesService;
import com.desafioitau.api.transferencia.service.ContaService;
import com.desafioitau.api.transferencia.service.transferencia.strategy.CheckTransferenciaContasInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferenciaService {

    private final ContaService contaService;

    private final CadastroClientesService cadastroClientesService;

    private final ModelMapper modelMapper;

    private final BacenService bacenService;

    private final List<CheckTransferenciaContasInterface> strategies;

    @Autowired
    public TransferenciaService(ContaService contaService,
                                CadastroClientesService cadastroClientesService,
                                ModelMapper modelMapper, BacenService bacenService,
                                List<CheckTransferenciaContasInterface> strategies) {
        this.contaService = contaService;
        this.cadastroClientesService = cadastroClientesService;
        this.modelMapper = modelMapper;
        this.bacenService = bacenService;
        this.strategies = strategies;
    }

    public void transfer(TransferenciaRequestDTO transferenciaRequestDTO) {
        TransferenciaEntity transferencia = modelMapper.map(transferenciaRequestDTO, TransferenciaEntity.class);
        ClienteEntity clienteDestino = cadastroClientesService.getClientById(transferencia.getIdCliente());
        ContaEntity contaOrigem = contaService.getContaById(transferencia.getConta().getIdOrigem());

        strategies.forEach(strategy -> strategy.checkTransferenciaContas(transferencia, contaOrigem));

        double valorTotal = contaOrigem.getSaldo() - transferencia.getValor();
        SaldoEntity saldo = SaldoEntity.builder()
                .valor(valorTotal)
                .nomeDestino(clienteDestino.getNome())
                .conta(transferencia.getConta().getIdOrigem(), transferencia.getConta().getIdDestino())
                .build();
        contaService.updateSaldoConta(saldo);

        NotificacaoEntity notificacao = NotificacaoEntity.builder()
                .valor(transferencia.getValor())
                .conta(transferencia.getConta().getIdOrigem(), transferencia.getConta().getIdDestino())
                .build();
        bacenService.notificarTransferencia(notificacao);
    }

}
