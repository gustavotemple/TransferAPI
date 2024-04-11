package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ClientNotFoundException;
import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.entity.ClienteEntity;
import com.desafioitau.api.transferencia.repository.CadastroClientesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CadastroClientesServiceTest {

    @Mock
    private CadastroClientesRepository clientesRepository;

    @InjectMocks
    private CadastroClientesService clientesService;

    private ClienteEntity cliente;

    @BeforeEach
    public void setup() {
        cliente = new ClienteEntity();
        cliente.setId("bcdd1048-a501-4608-bc82-66d7b4db3600");
    }

    @Test
    public void shouldReturnClienteWithSuccess() {
        String id = "bcdd1048-a501-4608-bc82-66d7b4db3600";

        when(clientesRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteEntity clienteEntity = clientesService.getClientById(id);

        assertEquals(clienteEntity.getId(), cliente.getId());
    }

    @Test
    public void shouldThrowExceptionWhenClienteNotFound() {
        assertThrows(ClientNotFoundException.class, () -> clientesService.getClientById("not found"));
    }

    @Test
    public void shouldThrowExceptionWhenClientesRepositoryFail() {
        String id = "error";

        doThrow(ServiceUnavailableException.class).when(clientesRepository).findById(id);

        assertThrows(ServiceUnavailableException.class, () -> clientesService.getClientById(id));
    }
}
