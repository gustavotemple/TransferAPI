package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.service.transferencia.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @Autowired
    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping("/transferencia")
    public ResponseEntity<TransferenciaResponseDTO> efetuarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO)
    {
        // implementar serviço de transferência
        transferenciaService.transfer(transferenciaRequestDTO);

        // retornar o idTransferencia
        UUID uuid = UUID.randomUUID();
        TransferenciaResponseDTO transferenciaResponseDTO = new TransferenciaResponseDTO();
        transferenciaResponseDTO.setIdTransferencia(uuid);
        return ResponseEntity.ok().body(transferenciaResponseDTO);
    }
}
