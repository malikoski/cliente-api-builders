package br.com.builders.cliente.mapper;

import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.model.UnidadeFederacao;
import br.com.builders.cliente.resource.dto.*;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    Cliente requestToDomain(ClienteRequest request);

    ClienteResponse modelToResponse(Cliente cliente);

    default String map(UUID value) {
        return value != null ? value.toString() : null;
    }

    Uf unidadeFederacaoToUf(UnidadeFederacao unidadeFederacao);

    List<ClienteResponse> clientesToListResponse(List<Cliente> clientes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClienteFromRequest(ClienteUpdateRequest request, @MappingTarget Cliente cliente);

    default Pageable pageableDomainToDto(org.springframework.data.domain.Pageable pageable) {
        return Pageable.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .build();
    }

    default PaginationClienteResponse pageClienteToPaginationResponse(Page page) {
        return PaginationClienteResponse.builder()
                .content(page.getContent())
                .pageable(pageableDomainToDto(page.getPageable()))
                .totalElements(page.getTotalElements())
                .build();
    }
}
