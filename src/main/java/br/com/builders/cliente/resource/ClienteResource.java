package br.com.builders.cliente.resource;

import br.com.builders.cliente.resource.dto.ClienteRequest;
import br.com.builders.cliente.resource.dto.ClienteResponse;
import br.com.builders.cliente.resource.dto.ClienteUpdateRequest;
import br.com.builders.cliente.resource.dto.PaginationClienteResponse;
import br.com.builders.cliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClienteResource implements ClienteApi {

    private final ClienteService clienteService;

    @Override
    public ResponseEntity<ClienteResponse> addCliente(@Validated @RequestBody ClienteRequest clienteRequest) {
        log.info("addCliente(clienteRequest), Adicionando cliente: {}", clienteRequest);

        return ofNullable(clienteService.save(clienteRequest))
                .map(response -> new ResponseEntity(response, HttpStatus.CREATED))
                .get();
    }

    @Override
    public ResponseEntity<ClienteResponse> findClienteByUuid(@PathVariable("uuid") String uuid) {
        log.info("findClienteByUuid(uuid), Procurando cliente: {}", uuid);

        return ofNullable(clienteService.findByUuid(UUID.fromString(uuid)))
                .map(response -> new ResponseEntity(response, HttpStatus.OK))
                .get();
    }

    @Override
    public ResponseEntity<PaginationClienteResponse> getAllClientes(Integer page, Integer size, Optional<String> search) {
        log.info("getAllClientes(pageNumber, pageSize, search) - Consultando todos os clientes - " +
                "pageNumber: {}, pageSize: {}, search: {}", page, size, search);

        return ofNullable(clienteService.findAll(page, size, search))
                .map(ResponseEntity::ok)
                .get();
    }

    @Override
    public ResponseEntity<ClienteResponse> updateCliente(@Validated @PathVariable("uuid") String uuid, @Validated @RequestBody ClienteUpdateRequest clienteUpdateRequestRequest) {
        log.info("updateCliente(uuid, clienteRequest) - Atualizando cliente: {}", clienteUpdateRequestRequest);

        return ofNullable(clienteService.update(uuid, clienteUpdateRequestRequest))
                .map(ResponseEntity::ok)
                .get();
    }

}

