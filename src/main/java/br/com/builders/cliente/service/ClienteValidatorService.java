package br.com.builders.cliente.service;

import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.validator.OnCreate;
import br.com.builders.cliente.validator.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

@Service
@Validated
@RequiredArgsConstructor
public class ClienteValidatorService {

    private final Validator validator;

    @Validated(OnCreate.class)
    Cliente validateForCreate(@Valid Cliente cliente) {
        return validateModel(cliente);
    }

    @Validated(OnUpdate.class)
    Cliente validateForUpdate(@Valid Cliente cliente) {
        return validateModel(cliente);
    }

    private Cliente validateModel(final Cliente cliente) {
        var violations = validator.validate(cliente);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return cliente;
    }
}
