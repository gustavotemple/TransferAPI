package com.desafioitau.api.transferencia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class NotificacaoEntity {

    private double valor;
    private Conta conta;

    private NotificacaoEntity() {
    }

    public static Builder builder() {
        return new NotificacaoEntity().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder valor(double valor) {
            NotificacaoEntity.this.valor = valor;
            return this;
        }

        public Builder conta(String idOrigem, String idDestino) {
            NotificacaoEntity.this.conta = new Conta(idOrigem, idDestino);
            return this;
        }

        public NotificacaoEntity build() {
            return NotificacaoEntity.this;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}
