package br.com.builders.cliente.helper;

import br.com.builders.cliente.mapper.ClienteMapper;
import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.model.UnidadeFederacao;
import br.com.builders.cliente.resource.dto.*;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

public class ClienteTestHelper {

    static ClienteMapper clienteMapper = Mappers.getMapper(ClienteMapper.class);

    public static final UUID UUID = java.util.UUID.fromString("f73293ac-7b69-4b6c-81a2-0bdb11f93760");
    public static final UUID UUID_2 = java.util.UUID.fromString("1b6ee77e-388e-4e1b-8e34-692b634df309");
    public static final UUID UUID_3 = java.util.UUID.fromString("5af444ad-7353-4853-936b-fac128c5430b");


    public static PaginationClienteResponse createPageClienteResponse() {
        var clienteOne = createClienteResponse();
        var clienteTwo = createClienteResponse();

        clienteTwo.setUuid(UUID_2.toString());
        clienteTwo.setNome("João Santos");

        var pageable = PageRequest.of(0, 20);

        var page = new PageImpl<>(List.of(clienteOne, clienteTwo), pageable, 0);
        return clienteMapper.pageClienteToPaginationResponse(page);
    }

    public static ClienteResponse createClienteResponse() {
        var cliente = Cliente.builder()
                .id(1)
                .uuid(UUID)
                .nome("Silvio Santos")
                .idade(90)
                .endereco("Av. Paulista")
                .numero("1000")
                .cidade("São Paulo")
                .uf(UnidadeFederacao.SP)
                .cpf("09425214042")
                .cep("01153000")
                .telefone("1199990000")
                .email("silviosantos@sbt.com.br")
                .build();

        return clienteMapper.modelToResponse(cliente);
    }

    public static ClienteRequest createClientRequest() {
        return ClienteRequest.builder()
                .nome("Silvio Santos")
                .idade(90)
                .endereco("Av. Paulista")
                .numero("1000")
                .cidade("São Paulo")
                .uf(Uf.SP)
                .cpf("09425214042")
                .cep("01153000")
                .telefone("1199990000")
                .email("silviosantos@sbt.com.br")
                .build();
    }

    public static ClienteUpdateRequest createUpdateClientRequest() {
        return ClienteUpdateRequest.builder()
                .nome("Silvio Santos")
                .idade(90)
                .endereco("Av. Paulista")
                .numero("1000")
                .cidade("São Paulo")
                .uf(Uf.SP)
                .cep("01153000")
                .telefone("1199990000")
                .email("silviosantos@sbt.com.br")
                .build();
    }

    public static Cliente createCliente() {
        return Cliente.builder()
                .id(1)
                .uuid(UUID)
                .nome("Silvio Santos")
                .idade(90)
                .endereco("Av. Paulista")
                .numero("1000")
                .cidade("São Paulo")
                .uf(UnidadeFederacao.SP)
                .cpf("09425214042")
                .cep("01153000")
                .telefone("1199990000")
                .email("silviosantos@sbt.com.br")
                .build();
    }

    public static ClienteRequest createClientRequestInvalid() {
        return ClienteRequest.builder()
                .nome("ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ")
                .idade(200)
                .endereco("ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ")
                .numero("10000000001")
                .cidade("ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ ABCDEFGHIJKLMNOPQRSTUVXYZ")
                .uf(Uf.SP)
                .cpf("1234567890123")
                .cep("0115300099999")
                .telefone("1199990000")
                .email("email_invalido")
                .build();
    }

    public static List<Cliente> createListClientes() {
        var clientes = new ArrayList<Cliente>();
        var fakeValuesService = new FakeValuesService(new Locale("pt-BR"), new RandomService());
        var faker = new Faker();
        for (int i = 0; i < 20; i++) {
            var cliente = Cliente.builder()
                    .nome(faker.name().fullName())
                    .idade(faker.random().nextInt(80))
                    .endereco(faker.address().streetName())
                    .numero(faker.address().buildingNumber())
                    .cidade(faker.address().city())
                    .uf(randomEnum(UnidadeFederacao.class))
                    .cpf(CpfHelper.generateCPF(true))
                    .cep(faker.address().zipCode())
                    .telefone(faker.phoneNumber().cellPhone())
                    .email(fakeValuesService.bothify("????##@gmail.com"))
                    .build();

            clientes.add(cliente);
        }

        return clientes;
    }

    public static Cliente toModel(ClienteRequest clienteRequest) {
        return clienteMapper.requestToDomain(clienteRequest);
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
