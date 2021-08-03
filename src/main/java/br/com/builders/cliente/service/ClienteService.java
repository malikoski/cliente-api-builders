package br.com.builders.cliente.service;

import br.com.builders.cliente.exception.ClienteRuntimeException;
import br.com.builders.cliente.mapper.ClienteMapper;
import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.repository.specification.cliente.ClienteSpecificationBuilder;
import br.com.builders.cliente.repository.specification.SearchOperation;
import br.com.builders.cliente.resource.dto.ClienteRequest;
import br.com.builders.cliente.resource.dto.ClienteResponse;
import br.com.builders.cliente.resource.dto.ClienteUpdateRequest;
import br.com.builders.cliente.resource.dto.PaginationClienteResponse;
import com.google.common.base.Joiner;
import io.vavr.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Validated
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteValidatorService validatorService;

    public ClienteResponse save(final ClienteRequest clienteRequest) {
        return of(clienteRequest)
                .map(clienteMapper::requestToDomain)
                .map(validatorService::validateForCreate)
                .map(clienteRepository::save)
                .map(clienteMapper::modelToResponse)
                .get();
    }

    public ClienteResponse findByUuid(final UUID uuid) {
        return of(uuid)
                .flatMap(clienteRepository::findByUuid)
                .map(clienteMapper::modelToResponse)
                .orElseThrow(() -> new ClienteRuntimeException("Cliente não encontrado."));
    }

    @Cacheable(value = "clientes", key = "#page.toString().concat('-').concat(#size.toString()).concat(#search.orElse(''))")
    public PaginationClienteResponse findAll(final Integer page, final Integer size, final Optional<String> search) {

        var pageable = PageRequest.of(page, size);

        var clientesResponse = search
                .map(this::builderSpecification)
                .map(spec -> clienteRepository.findAll(spec, pageable))
                .or(() -> Optional.of(clienteRepository.findAll(pageable)));

        return clientesResponse
                .map(response -> Tuple.of(clienteMapper.clientesToListResponse(response.getContent()), response.getTotalElements()))
                .map(tuple -> new PageImpl(tuple._1, pageable, tuple._2))
                .map(clienteMapper::pageClienteToPaginationResponse)
                .get();
    }

    @CachePut(value = "clientes")
    public ClienteResponse update(@Valid String uuid, ClienteUpdateRequest clienteUpdateRequestRequest) {
        return of(UUID.fromString(uuid))
                .map(clienteRepository::findByUuid)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."))
                .map(cliente -> updateModel(clienteUpdateRequestRequest, cliente))
                .map(validatorService::validateForUpdate)
                .map(clienteRepository::save)
                .map(clienteMapper::modelToResponse)
                .get();
    }

    private Cliente updateModel(ClienteUpdateRequest clienteUpdateRequestest, Cliente cliente) {
        clienteMapper.updateClienteFromRequest(clienteUpdateRequestest, cliente);
        return cliente;
    }

    private Specification<Cliente> builderSpecification(final String search) {

        var builder = new ClienteSpecificationBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.getAllOperations());
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4));
        }

        return builder.build();
    }
}
