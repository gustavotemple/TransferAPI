package com.desafioitau.api.transferencia.repository;

import com.desafioitau.api.transferencia.entity.ContaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContasRepositoryTest {

    @Mock
    private ContasRepository contasRepository;

    private ContaEntity contaEntity;

    @BeforeEach
    public void setup() {
        String id = "d0d32142-74b7-4aca-9c68-838aeacef96b";
        contaEntity = new ContaEntity();
        contaEntity.setId(id);
        contaEntity.setAtivo(true);
    }

    @Test
    public void shouldIdMatchWhenFindById() {
        String id = "d0d32142-74b7-4aca-9c68-838aeacef96b";
        when(contasRepository.findById(id)).thenReturn(Optional.of(contaEntity));

        Optional<ContaEntity> clienteOptional = contasRepository.findById(id);

        assertTrue(clienteOptional.isPresent());
        assertTrue(clienteOptional.get().isAtivo());
        assertEquals(id, clienteOptional.get().getId());
    }

    @Test
    public void shouldIdNotMatchWhenFindById() {
        String id = "ops";
        Optional<ContaEntity> clienteOptional = contasRepository.findById(id);

        assertFalse(clienteOptional.isPresent());
    }

}
