package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ClientNotFoundException;
import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.entity.ClienteEntity;
import com.desafioitau.api.transferencia.repository.CadastroClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.util.Optional;

@Service
public class CadastroClientesService {

    private final CadastroClientesRepository clientesReposiory;

    @Autowired
    public CadastroClientesService(CadastroClientesRepository clientesReposiory) {
        this.clientesReposiory = clientesReposiory;
    }

    public ClienteEntity getClientById(String idCliente) {
        Optional<ClienteEntity> cliente;
        try {
            cliente = clientesReposiory.findById(idCliente);
        } catch (Exception e) {
            Logger.error(e);
            throw new ServiceUnavailableException(CadastroClientesService.class.getName());
        }

        if (cliente.isEmpty()) {
            throw new ClientNotFoundException(idCliente);
        }

        return cliente.get();
    }

}
