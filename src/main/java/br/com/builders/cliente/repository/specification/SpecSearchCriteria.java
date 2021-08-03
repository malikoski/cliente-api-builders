package br.com.builders.cliente.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
}