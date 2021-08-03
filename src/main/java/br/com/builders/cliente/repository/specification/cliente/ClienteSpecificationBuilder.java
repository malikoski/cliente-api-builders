package br.com.builders.cliente.repository.specification.cliente;

import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.repository.specification.SearchOperation;
import br.com.builders.cliente.repository.specification.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClienteSpecificationBuilder {

    private final List<SpecSearchCriteria> params;

    public ClienteSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public final ClienteSpecificationBuilder with(final String key, final String operation, final Object value) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        params.add(new SpecSearchCriteria(key, op, value));
        return this;
    }

    public Specification<Cliente> build() {
        if (params.size() == 0)
            return null;

        Specification<Cliente> result = new ClienteSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new ClienteSpecification(params.get(i)));
        }

        return result;
    }
}