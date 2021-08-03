package br.com.builders.cliente.validator.impl;

import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.validator.UniqueCPF;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    private final ClienteRepository clienteRepository;

    @Override
    public void initialize(UniqueCPF constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        return cpf != null && !clienteRepository.findByCpf(cpf).isPresent();
    }
}
