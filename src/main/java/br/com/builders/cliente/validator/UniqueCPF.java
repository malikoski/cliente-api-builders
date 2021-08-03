package br.com.builders.cliente.validator;

import br.com.builders.cliente.validator.impl.UniqueCPFValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCPFValidator.class)
public @interface UniqueCPF {
    String message() default "{br.com.builders.cliente.UniqueCPF.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
