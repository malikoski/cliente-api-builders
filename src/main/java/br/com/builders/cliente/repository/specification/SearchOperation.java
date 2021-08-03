package br.com.builders.cliente.repository.specification;

import br.com.builders.cliente.model.Cliente;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum SearchOperation implements SearchOperationUtilSpecification {
    EQUALITY {
        @Override
        public char getSimpleOperation() {
            return ':';
        }

        @Override
        public Predicate toPredicate(final SpecSearchCriteria criteria, final Root<Cliente> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
    },

    NEGATION {
        @Override
        public char getSimpleOperation() {
            return '!';
        }

        @Override
        public Predicate toPredicate(SpecSearchCriteria criteria, Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
        }
    },

    GREATER_THAN {
        @Override
        public char getSimpleOperation() {
            return '>';
        }

        @Override
        public Predicate toPredicate(SpecSearchCriteria criteria, Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },

    LESS_THAN {
        @Override
        public char getSimpleOperation() {
            return '<';
        }

        @Override
        public Predicate toPredicate(SpecSearchCriteria criteria, Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },

    CONTAINS {
        @Override
        public char getSimpleOperation() {
            return '~';
        }

        @Override
        public Predicate toPredicate(SpecSearchCriteria criteria, Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
    },

    ;

    public static final String[] getAllOperations() {
        return stream(SearchOperation.values())
                .map(o -> String.valueOf(o.getSimpleOperation()))
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }

    //public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };


    public static SearchOperation getSimpleOperation(final char input) {
        for (SearchOperation operation : SearchOperation.values()) {
            if (operation.getSimpleOperation() == input)
                return operation;
        }

        return null;
    }
}
