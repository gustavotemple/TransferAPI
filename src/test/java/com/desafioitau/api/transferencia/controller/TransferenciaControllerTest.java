package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.service.transferencia.TransferenciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = TransferenciaController.class)
public class TransferenciaControllerTest {

    private final WebTestClient webTestClient;

    @MockBean
    private TransferenciaService transferenciaService;

    @Autowired
    public TransferenciaControllerTest(WebTestClient webTestClient,
                                       TransferenciaService transferenciaService) {
        this.webTestClient = webTestClient;
        this.transferenciaService = transferenciaService;
    }

    @Test
    public void shouldDoTransferencia() {
        webTestClient.post().uri("/transferencia")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TransferenciaRequestDTO())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransferenciaResponseDTO.class)
                .value(transferenciaResponseDTO -> {
                    assert transferenciaResponseDTO != null;
                });
    }

}
