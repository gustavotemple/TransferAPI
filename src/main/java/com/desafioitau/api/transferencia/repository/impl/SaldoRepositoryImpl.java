package com.desafioitau.api.transferencia.repository.impl;

import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.entity.SaldoEntity;
import com.desafioitau.api.transferencia.repository.SaldoRepository;
import com.desafioitau.api.transferencia.util.RetryUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.tinylog.Logger;
import reactor.util.retry.Retry;

@Component
public class SaldoRepositoryImpl implements SaldoRepository {

    private final WebClient webClient;
    private final RetryUtils retryUtils;

    private final ModelMapper modelMapper;


    @Autowired
    public SaldoRepositoryImpl(WebClient.Builder webClientBuilder,
                               RetryUtils retryUtils,
                               ModelMapper modelMapper) {
        this.webClient = webClientBuilder.build();
        this.retryUtils = retryUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public void update(SaldoEntity saldo) {
        SaldoRequestDTO saldoRequestDTO = modelMapper.map(saldo, SaldoRequestDTO.class);
        Logger.debug(saldoRequestDTO);
        webClient.put()
                .uri("/contas/saldos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(saldoRequestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.backoff(retryUtils.getMaxAttempts(),
                                retryUtils.getMinBackoffDuration())
                        .filter(retryUtils::isRetryableError))
                .timeout(retryUtils.getTimeoutDuration())
                .block();
    }

}
