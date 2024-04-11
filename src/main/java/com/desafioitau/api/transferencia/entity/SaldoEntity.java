package com.desafioitau.api.transferencia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaldoEntity {

    private double valor;
    private String nomeDestino;
    private ContaEntity conta;

    private SaldoEntity() {
    }

    public static Builder builder() {
        return new SaldoEntity().new Builder();
   }

    public class Builder {
        private Builder() {
        }

        public Builder valor(double valor) {
            SaldoEntity.this.valor = valor;
            return this;
        }

        public Builder nomeDestino(String nomeDestino) {
            SaldoEntity.this.nomeDestino = nomeDestino;
            return this;
        }

        public Builder conta(String idOrigem, String idDestino) {
            SaldoEntity.this.conta = new ContaEntity(idOrigem, idDestino);
            return this;
        }

        public SaldoEntity build() {
            return SaldoEntity.this;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ContaEntity {
        private String idOrigem;
        private String idDestino;
    }
}
