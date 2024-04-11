package com.desafioitau.api.transferencia.repository;

import com.desafioitau.api.transferencia.entity.ClienteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CadastroClientesRepositoryTest {

    @Mock
    private CadastroClientesRepository clientesRepository;

    private ClienteEntity clienteEntity;

    @BeforeEach
    public void setup() {
        clienteEntity = new ClienteEntity();
        clienteEntity.setId("bcdd1048-a501-4608-bc82-66d7b4db3600");
        clienteEntity.setNome("João Silva");
    }

    @Test
    public void shouldIdMatchWhenFindById() {
        String id = "bcdd1048-a501-4608-bc82-66d7b4db3600";
        when(clientesRepository.findById(id)).thenReturn(Optional.of(clienteEntity));

        Optional<ClienteEntity> clienteOptional = clientesRepository.findById(id);

        assertTrue(clienteOptional.isPresent());
        assertEquals(id, clienteOptional.get().getId());
        assertEquals("João Silva", clienteOptional.get().getNome());
    }

    @Test
    public void shouldIdNotMatchWhenGetById() {
        String id = "ops";
        Optional<ClienteEntity> clienteOptional = clientesRepository.findById(id);

        assertFalse(clienteOptional.isPresent());
    }

}
