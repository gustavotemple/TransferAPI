package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.config.exception.ServiceUnavailableException;
import com.desafioitau.api.transferencia.entity.NotificacaoEntity;
import com.desafioitau.api.transferencia.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class BacenService {

    private final NotificacaoRepository notificacaoRepository;

    @Autowired
    public BacenService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void notificarTransferencia(NotificacaoEntity notificacao) {
        try {
            notificacaoRepository.save(notificacao);
        } catch (Exception e) {
            Logger.error(e);
            throw new ServiceUnavailableException(BacenService.class.getName());
        }
    }

}
