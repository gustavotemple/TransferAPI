package com.desafioitau.api.transferencia.repository;

import com.desafioitau.api.transferencia.entity.ContaEntity;

import java.util.Optional;

public interface ContasRepository {

    Optional<ContaEntity> findById(String id);

}
