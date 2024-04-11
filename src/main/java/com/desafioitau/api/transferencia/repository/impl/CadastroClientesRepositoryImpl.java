package com.desafioitau.api.transferencia.repository.impl;

import com.desafioitau.api.transferencia.config.exception.ClientNotFoundException;
import com.desafioitau.api.transferencia.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.entity.ClienteEntity;
import com.desafioitau.api.transferencia.repository.CadastroClientesRepository;
import com.desafioitau.api.transferencia.util.RetryUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.tinylog.Logger;
import reactor.util.retry.Retry;

import java.util.Optional;

@Component
public class CadastroClientesRepositoryImpl implements CadastroClientesRepository {

    private final WebClient webClient;
    private final RetryUtils retryUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public CadastroClientesRepositoryImpl(WebClient.Builder webClientBuilder,
                                          RetryUtils retryUtils,
                                          ModelMapper modelMapper) {
        this.webClient = webClientBuilder.build();
        this.retryUtils = retryUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<ClienteEntity> findById(String idCliente) {
        ClienteResponseDTO clienteResponseDTO;
        try {
            clienteResponseDTO = webClient.get()
                    .uri("/clientes/{id}", idCliente)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> response.bodyToMono(String.class)
                            .map(ClientNotFoundException::new))
                    .bodyToMono(ClienteResponseDTO.class)
                    .retryWhen(Retry.backoff(retryUtils.getMaxAttempts(), retryUtils.getMinBackoffDuration())
                            .filter(retryUtils::isRetryableError))
                    .timeout(retryUtils.getTimeoutDuration())
                    .block();
        } catch (ClientNotFoundException e) {
            Logger.error(e);
            return Optional.empty();
        }

        return Optional.of(modelMapper.map(clienteResponseDTO, ClienteEntity.class));
    }
}
