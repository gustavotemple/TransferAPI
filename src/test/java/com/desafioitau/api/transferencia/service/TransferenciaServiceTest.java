package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.conta.ContaDailyLimitException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaInactiveException;
import com.desafioitau.api.transferencia.config.exception.conta.ContaSaldoInsufficientException;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.entity.*;
import com.desafioitau.api.transferencia.service.transferencia.TransferenciaService;
import com.desafioitau.api.transferencia.service.transferencia.strategy.CheckTransferenciaContasInterface;
import com.desafioitau.api.transferencia.service.transferencia.strategy.ContaDailyLimit;
import com.desafioitau.api.transferencia.service.transferencia.strategy.ContaInactive;
import com.desafioitau.api.transferencia.service.transferencia.strategy.ContaSaldoInsufficient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTest {

    @Mock
    private ContaService contaService;

    @Mock
    private CadastroClientesService cadastroClientesService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BacenService bacenService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Captor
    private ArgumentCaptor<SaldoEntity> saldoEntityCaptor;

    @Captor
    private ArgumentCaptor<NotificacaoEntity> notificacaoEntityCaptor;

    TransferenciaRequestDTO requestDTO;

    TransferenciaEntity transferencia;

    @BeforeEach
    public void setup() {
        List<CheckTransferenciaContasInterface> strategies =
                Arrays.asList(new ContaDailyLimit(),
                        new ContaInactive(),
                        new ContaSaldoInsufficient());

        ReflectionTestUtils.setField(transferenciaService, "strategies", strategies);

        requestDTO = new TransferenciaRequestDTO();
        requestDTO.setIdCliente("2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f");
        requestDTO.setValor(10d);
        TransferenciaRequestDTO.Conta contaDTO = new TransferenciaRequestDTO.Conta();
        contaDTO.setIdDestino("d0d32142-74b7-4aca-9c68-838aeacef96b");
        contaDTO.setIdOrigem("41313d7b-bd75-4c75-9dea-1f4be434007f");
        requestDTO.setConta(contaDTO);

        transferencia = new TransferenciaEntity();
        transferencia.setIdCliente(requestDTO.getIdCliente());
        transferencia.setValor(requestDTO.getValor());
        TransferenciaEntity.Conta conta = new TransferenciaEntity.Conta();
        conta.setIdDestino(contaDTO.getIdDestino());
        conta.setIdOrigem(contaDTO.getIdOrigem());
        transferencia.setConta(conta);
    }

    @Test
    public void shouldThrowContaSaldoInsufficientException() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(0d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        assertThrows(ContaSaldoInsufficientException.class, () -> transferenciaService.transfer(requestDTO));
    }

    @Test
    public void shouldThrowContaInactiveException() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(false);
        contaOrigem.setSaldo(0d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        assertThrows(ContaInactiveException.class, () -> transferenciaService.transfer(requestDTO));
    }

    @Test
    public void shouldThrowContaDailyLimitException() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(0d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(100d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        assertThrows(ContaDailyLimitException.class, () -> transferenciaService.transfer(requestDTO));
    }

    @Test
    public void shouldCallBacenNotificarTransferencia() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(100d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        transferenciaService.transfer(requestDTO);

        verify(bacenService).notificarTransferencia(any(NotificacaoEntity.class));
    }

    @Test
    public void shouldCallUpdateSaldoConta() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(100d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        transferenciaService.transfer(requestDTO);

        verify(contaService).updateSaldoConta(any(SaldoEntity.class));
    }

    @Test
    public void shouldMatchSaldoValues() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());
        clienteDestino.setNome("Nome");

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(100d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        transferenciaService.transfer(requestDTO);

        double valorTotal = contaOrigem.getSaldo() - transferencia.getValor();

        verify(contaService).updateSaldoConta(saldoEntityCaptor.capture());
        SaldoEntity capturedSaldoEntity = saldoEntityCaptor.getValue();
        assertEquals(valorTotal, capturedSaldoEntity.getValor());
        assertEquals(clienteDestino.getNome(), capturedSaldoEntity.getNomeDestino());
    }

    @Test
    public void shouldMatchBacenNotificacaoValue() {
        ClienteEntity clienteDestino = new ClienteEntity();
        clienteDestino.setId(transferencia.getIdCliente());

        ContaEntity contaOrigem = new ContaEntity();
        contaOrigem.setId(transferencia.getConta().getIdOrigem());
        contaOrigem.setLimiteDiario(100d);
        contaOrigem.setAtivo(true);
        contaOrigem.setSaldo(100d);

        when(modelMapper.map(requestDTO, TransferenciaEntity.class)).thenReturn(transferencia);
        when(cadastroClientesService.getClientById(transferencia.getIdCliente())).thenReturn(clienteDestino);
        when(contaService.getContaById(transferencia.getConta().getIdOrigem())).thenReturn(contaOrigem);

        transferenciaService.transfer(requestDTO);

        verify(bacenService).notificarTransferencia(notificacaoEntityCaptor.capture());
        NotificacaoEntity notificacaoEntityCaptorValue = notificacaoEntityCaptor.getValue();
        assertEquals(transferencia.getValor(), notificacaoEntityCaptorValue.getValor());
    }

}
