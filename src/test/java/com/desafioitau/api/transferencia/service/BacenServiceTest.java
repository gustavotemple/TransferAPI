package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.entity.NotificacaoEntity;
import com.desafioitau.api.transferencia.repository.NotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BacenServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private BacenService bacenService;

    @Captor
    private ArgumentCaptor<NotificacaoEntity> notificacaoEntityCaptor;

    private NotificacaoEntity notificacao;

    @BeforeEach
    public void setup() {
        notificacao = NotificacaoEntity.builder()
                .valor(10d)
                .build();
    }

    @Test
    public void shouldNotifyTransferenciaWithSuccess() {
        bacenService.notificarTransferencia(notificacao);

        verify(notificacaoRepository).save(notificacaoEntityCaptor.capture());
        NotificacaoEntity notificacaoEntityCaptorValue = notificacaoEntityCaptor.getValue();
        assertEquals(notificacao.getValor(), notificacaoEntityCaptorValue.getValor());
    }

    @Test
    public void shouldThrowExceptionWhenRepositoryFail() {
        doThrow(ServiceUnavailableException.class).when(notificacaoRepository).save(notificacao);

        assertThrows(ServiceUnavailableException.class, () -> bacenService.notificarTransferencia(notificacao));
    }
}
