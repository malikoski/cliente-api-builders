package br.com.builders.cliente.service;

import br.com.builders.cliente.helper.ClienteTestHelper;
import br.com.builders.cliente.mapper.ClienteMapper;
import br.com.builders.cliente.model.Cliente;
import br.com.builders.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClienteServiceTest {

    private static final String UUID_INVALIDO = "INVALIDO";
    public static final String NOME_ALTERADO = "Nome Alterado";
    public static final int IDADE_ALTERADA = 50;

    @Autowired
    ClienteService clienteService;

    @MockBean
    ClienteRepository clienteRepository;

    ClienteMapper clienteMapper = Mappers.getMapper(ClienteMapper.class);

    @Test
    @DisplayName("Deve salvar um cliente")
    public void saveClienteComSucesso() {
        var clienteRequest = ClienteTestHelper.createClientRequest();
        var cliente = ClienteTestHelper.toModel(clienteRequest).setId(1);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clientResponse = clienteService.save(clienteRequest);

        assertThat(clientResponse.getNome()).isEqualTo(clienteRequest.getNome());
        assertThat(clientResponse.getIdade()).isEqualTo(clienteRequest.getIdade());
        assertThat(clientResponse.getEndereco()).isEqualTo(clienteRequest.getEndereco());
        assertThat(clientResponse.getNumero()).isEqualTo(clienteRequest.getNumero());
        assertThat(clientResponse.getCidade()).isEqualTo(clienteRequest.getCidade());
        assertThat(clientResponse.getUf()).isEqualTo(clienteRequest.getUf());
        assertThat(clientResponse.getCpf()).isEqualTo(clienteRequest.getCpf());
        assertThat(clientResponse.getCep()).isEqualTo(clienteRequest.getCep());
        assertThat(clientResponse.getTelefone()).isEqualTo(clienteRequest.getTelefone());
        assertThat(clientResponse.getEmail()).isEqualTo(clienteRequest.getEmail());

        verify(clienteRepository, times(1)).findByCpf((any(String.class)));
    }

    @Test
    @DisplayName("Deve retornar que o cpf já existe ao tentar salvar um novo cliente")
    public void newClienteComCpfJaExistente() {
        var clienteRequest = ClienteTestHelper.createClientRequest();
        var cliente = ClienteTestHelper.toModel(clienteRequest).setId(1);

        when(clienteRepository.findByCpf(any(String.class))).thenReturn(of(cliente));

        assertThrows(ConstraintViolationException.class, () -> {
            clienteService.save(clienteRequest);
        });
    }

/*    @Test
    @DisplayName("Deve retornar que o uuid é inválido ao tentar consultar um cliente com uuid não adequado")
    public void findClienteComUuidInvalido() {
        assertThrows(ConstraintViolationException.class, () -> {
            clienteService.findByUuid(UUID_INVALIDO);
        });
    }*/

    @Test
    @DisplayName("Deve retornar erro ao salvar um cliente com um cpf existente")
    public void updateClienteComSucesso() {

        var clienteUpdateRequest = ClienteTestHelper.createUpdateClientRequest()
                .nome(NOME_ALTERADO)
                .idade(IDADE_ALTERADA);

        var cliente = ClienteTestHelper.createCliente();

        when(clienteRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clientResponse = clienteService.update(cliente.getUuid().toString(), clienteUpdateRequest);

        assertThat(clientResponse.getNome()).isEqualTo(NOME_ALTERADO);
        assertThat(clientResponse.getIdade()).isEqualTo(IDADE_ALTERADA);
        assertThat(clientResponse.getEndereco()).isEqualTo(clienteUpdateRequest.getEndereco());
        assertThat(clientResponse.getNumero()).isEqualTo(clienteUpdateRequest.getNumero());
        assertThat(clientResponse.getCidade()).isEqualTo(clienteUpdateRequest.getCidade());
        assertThat(clientResponse.getUf()).isEqualTo(clienteUpdateRequest.getUf());
        assertThat(clientResponse.getCep()).isEqualTo(clienteUpdateRequest.getCep());
        assertThat(clientResponse.getTelefone()).isEqualTo(clienteUpdateRequest.getTelefone());
        assertThat(clientResponse.getEmail()).isEqualTo(clienteUpdateRequest.getEmail());

        verify(clienteRepository, times(0)).findByCpf((any(String.class)));
        verify(clienteRepository, times(1)).findByUuid((any(UUID.class)));
    }

}
