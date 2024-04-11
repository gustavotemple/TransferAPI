package com.desafioitau.api.transferencia.repository.impl;

import com.desafioitau.api.transferencia.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.entity.NotificacaoEntity;
import com.desafioitau.api.transferencia.repository.NotificacaoRepository;
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
public class NotificacaoRepositoryImpl implements NotificacaoRepository {

    private final WebClient webClient;
    private final RetryUtils retryUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public NotificacaoRepositoryImpl(WebClient.Builder webClientBuilder,
                                     RetryUtils retryUtils,
                                     ModelMapper modelMapper) {
        this.webClient = webClientBuilder.build();
        this.retryUtils = retryUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(NotificacaoEntity notificacao) {
        NotificacaoRequestDTO notificacaoRequestDTO = modelMapper.map(notificacao, NotificacaoRequestDTO.class);
        Logger.debug(notificacaoRequestDTO);
        webClient.post()
                .uri("/notificacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(notificacaoRequestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.backoff(retryUtils.getMaxAttempts(), retryUtils.getMinBackoffDuration())
                        .filter(retryUtils::isRetryableError))
                .timeout((retryUtils.getTimeoutDuration()))
                .block();
    }
}
