package br.com.builders.cliente.repository.specification.cliente;

import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.repository.specification.SpecSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class ClienteSpecification implements Specification<Cliente> {

    private SpecSearchCriteria criteria;

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<Cliente> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        return criteria.getOperation().toPredicate(criteria, root, query, builder);
    }
}