package br.com.builders.cliente.exception;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public ValidationErrorResponse addViolation(Violation violation) {
        violations.add(violation);
        return this;
    }
}
