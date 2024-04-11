package com.desafioitau.api.transferencia.repository;

import com.desafioitau.api.transferencia.entity.ClienteEntity;

import java.util.Optional;

public interface CadastroClientesRepository {

    Optional<ClienteEntity> findById(String id);

}
