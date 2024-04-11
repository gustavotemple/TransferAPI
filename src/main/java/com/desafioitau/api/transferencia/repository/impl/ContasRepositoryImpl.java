package com.desafioitau.api.transferencia.repository.impl;

import com.desafioitau.api.transferencia.config.exception.conta.ContaNotFoundException;
import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.SaldoEntity;
import com.desafioitau.api.transferencia.repository.ContasRepository;
import com.desafioitau.api.transferencia.util.RetryUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.tinylog.Logger;
import reactor.util.retry.Retry;

import java.util.Optional;

@Component
public class ContasRepositoryImpl implements ContasRepository {

    private final WebClient webClient;
    private final RetryUtils retryUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public ContasRepositoryImpl(WebClient.Builder webClientBuilder,
                                RetryUtils retryUtils,
                                ModelMapper modelMapper) {
        this.webClient = webClientBuilder.build();
        this.retryUtils = retryUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<ContaEntity> findById(String id) {
        ContaResponseDTO contaResponseDTO;
        try {
            contaResponseDTO = webClient.get()
                    .uri("/contas/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> response.bodyToMono(String.class)
                            .map(ContaNotFoundException::new))
                    .bodyToMono(ContaResponseDTO.class)
                    .retryWhen(Retry.backoff(retryUtils.getMaxAttempts(),
                                    retryUtils.getMinBackoffDuration())
                            .filter(retryUtils::isRetryableError))
                    .timeout(retryUtils.getTimeoutDuration())
                    .block();
        } catch (ContaNotFoundException e) {
            Logger.error(e);
            return Optional.empty();
        }

        return Optional.of(modelMapper.map(contaResponseDTO, ContaEntity.class));
    }

}
