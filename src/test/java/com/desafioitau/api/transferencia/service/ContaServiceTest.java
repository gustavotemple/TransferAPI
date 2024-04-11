package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaNotFoundException;
import com.desafioitau.api.transferencia.entity.ContaEntity;
import com.desafioitau.api.transferencia.entity.SaldoEntity;
import com.desafioitau.api.transferencia.repository.ContasRepository;
import com.desafioitau.api.transferencia.repository.SaldoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContasRepository contasRepository;

    @Mock
    private SaldoRepository saldoRepository;

    @InjectMocks
    private ContaService contaService;

    @Captor
    private ArgumentCaptor<SaldoEntity> saldoEntityCaptor;

    private ContaEntity conta;

    private SaldoEntity saldo;

    @BeforeEach
    public void setup() {
        conta = new ContaEntity();
        conta.setId("41313d7b-bd75-4c75-9dea-1f4be434007f");

        saldo = SaldoEntity.builder()
                .valor(10d)
                .nomeDestino("Oi")
                .build();
    }

    @Test
    public void shouldReturnContaWithSuccess() {
        String id = "41313d7b-bd75-4c75-9dea-1f4be434007f";

        when(contasRepository.findById(id)).thenReturn(Optional.of(conta));

        ContaEntity contaEntity = contaService.getContaById(id);

        assertEquals(contaEntity.getId(), conta.getId());
    }

    @Test
    public void shouldUpdateSaldoRepositoryWithSuccess() {
        contaService.updateSaldoConta(saldo);

        verify(saldoRepository).update(saldoEntityCaptor.capture());
        SaldoEntity capturedSaldoEntity = saldoEntityCaptor.getValue();
        assertEquals(saldo.getValor(), capturedSaldoEntity.getValor());
        assertEquals(saldo.getNomeDestino(), capturedSaldoEntity.getNomeDestino());
    }

    @Test
    public void shouldThrowExceptionWhenContaNotFound() {
        assertThrows(ContaNotFoundException.class, () -> contaService.getContaById("not found"));
    }

    @Test
    public void shouldThrowExceptionWhenContasRepositoryFail() {
        String id = "error";

        doThrow(ServiceUnavailableException.class).when(contasRepository).findById(id);

        assertThrows(ServiceUnavailableException.class, () -> contaService.getContaById(id));
    }

    @Test
    public void shouldThrowExceptionWhenSaldoRepositoryFail() {
        doThrow(ServiceUnavailableException.class).when(saldoRepository).update(saldo);

        assertThrows(ServiceUnavailableException.class, () -> contaService.updateSaldoConta(saldo));
    }
}
