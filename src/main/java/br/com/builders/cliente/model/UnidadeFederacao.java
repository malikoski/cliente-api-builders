package br.com.builders.cliente.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UnidadeFederacao {

    AM("Amazonas"),
    AL("Alagoas"),
    AC("Acre"),
    AP("Amapá"),
    BA("Bahia"),
    PA("Pará"),
    MT("Mato Grosso"),
    MG("Minas Gerais"),
    MS("Mato Grosso do Sul"),
    GO("Goiás"),
    MA("Maranhão"),
    RS("Rio Grande do Sul"),
    TO("Tocantins"),
    PI("Piauí"),
    SP("São Paulo"),
    RO("Rondônia"),
    RR("Roraima"),
    PR("Paraná"),
    CE("Ceará"),
    PE("Pernambuco"),
    SC("Santa Catarina"),
    PB("Paraíba"),
    RN("Rio Grande do Norte"),
    ES("Espírito Santo"),
    RJ("Rio de Janeiro"),
    SE("Sergipe"),
    DF("Distrito Federal");

    private final String nome;

    public static UnidadeFederacao fromNome(final String nomeUf) {
        for (final UnidadeFederacao uf : UnidadeFederacao.values()) {
            if (uf.nome.equalsIgnoreCase(nomeUf)) {
                return uf;
            }
        }
        throw new IllegalArgumentException(nomeUf);
    }

    public static UnidadeFederacao fromSigla(final String sigla) {
        for (final UnidadeFederacao uf : UnidadeFederacao.values()) {
            if (uf.toString().equalsIgnoreCase(sigla)) {
                return uf;
            }
        }
        throw new IllegalArgumentException(sigla);
    }
}
