package br.com.builders.cliente.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericErrorResponse {

    public String message;
}
