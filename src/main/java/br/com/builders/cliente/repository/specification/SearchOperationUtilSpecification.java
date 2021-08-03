package br.com.builders.cliente.repository.specification;

import br.com.builders.cliente.model.Cliente;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface SearchOperationUtilSpecification {

    char getSimpleOperation();
    Predicate toPredicate(final SpecSearchCriteria criteria, final Root<Cliente> root, final CriteriaQuery<?> query, final CriteriaBuilder builder);
}