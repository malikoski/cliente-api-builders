package br.com.builders.cliente.integration;

import br.com.builders.cliente.helper.ClienteTestHelper;
import br.com.builders.cliente.model.UnidadeFederacao;
import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.resource.dto.Uf;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ClienteOperationsInsertAndUpdateTest {

    static final String CLIENTES_API = "/clientes";
    private static final String OUTRO_NOME = "Outro Nome";
    private static final String OUTRO_EMAIL = "outro@email.com.br";
    private static final String MSG_CPF_INVALIDO = "CPF já existente.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um novo cliente")
    public void createNewCliente() throws Exception {

        var dto = ClienteTestHelper.createClientRequest();

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(
                        post(CLIENTES_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated());

        var clienteOptional = clienteRepository.findByCpf(dto.getCpf());

        assertThat(clienteOptional).isPresent();
        var cliente = clienteOptional.get();

        assertThat(cliente.getNome()).isEqualTo(dto.getNome());
        assertThat(cliente.getIdade()).isEqualTo(dto.getIdade());
        assertThat(cliente.getEndereco()).isEqualTo(dto.getEndereco());
        assertThat(cliente.getNumero()).isEqualTo(dto.getNumero());
        assertThat(cliente.getCidade()).isEqualTo(dto.getCidade());
        assertThat(cliente.getUf()).isEqualTo(UnidadeFederacao.fromSigla(dto.getUf().toString()));
        assertThat(cliente.getCpf()).isEqualTo(dto.getCpf());
        assertThat(cliente.getTelefone()).isEqualTo(dto.getTelefone());
        assertThat(cliente.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar gravar um cliente com cpf já cadastrado")
    public void createClienteCpfJaExistente() throws Exception {

        var dto = ClienteTestHelper.createClientRequest();

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(
                        post(CLIENTES_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated());

        var clienteOptional = clienteRepository.findByCpf(dto.getCpf());

        assertThat(clienteOptional).isPresent();
        var cliente = clienteOptional.get();

        assertThat(cliente.getNome()).isEqualTo(dto.getNome());
        assertThat(cliente.getIdade()).isEqualTo(dto.getIdade());
        assertThat(cliente.getEndereco()).isEqualTo(dto.getEndereco());
        assertThat(cliente.getNumero()).isEqualTo(dto.getNumero());
        assertThat(cliente.getCidade()).isEqualTo(dto.getCidade());
        assertThat(cliente.getUf()).isEqualTo(UnidadeFederacao.fromSigla(dto.getUf().toString()));
        assertThat(cliente.getCpf()).isEqualTo(dto.getCpf());
        assertThat(cliente.getTelefone()).isEqualTo(dto.getTelefone());
        assertThat(cliente.getEmail()).isEqualTo(dto.getEmail());

        dto.setNome(OUTRO_NOME);

        mockMvc.perform(
                        post(CLIENTES_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].message").value(MSG_CPF_INVALIDO));
    }

    @Test
    @DisplayName("Deve atualizar um cliente")
    public void updateCliente() throws Exception {

        var dto = ClienteTestHelper.createClientRequest();

        var cliente = ClienteTestHelper.createCliente();
        cliente = clienteRepository.save(cliente);

        /* ------ Update ----- */

        dto.nome(OUTRO_NOME)
                .idade(cliente.getIdade())
                .endereco(cliente.getEndereco())
                .numero(cliente.getNumero())
                .cidade(cliente.getCidade())
                .uf(Uf.fromValue(cliente.getUf().toString()))
                .cpf(cliente.getCpf())
                .cep(cliente.getCep())
                .telefone(cliente.getTelefone())
                .email(OUTRO_EMAIL);

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(
                        put(CLIENTES_API + "/" + cliente.getUuid().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk());

        var clienteUpdatedOptional = clienteRepository.findByCpf(dto.getCpf());

        assertThat(clienteUpdatedOptional).isPresent();
        var clienteUpdated = clienteUpdatedOptional.get();

        assertThat(clienteUpdated.getNome()).isEqualTo(OUTRO_NOME);
        assertThat(clienteUpdated.getIdade()).isEqualTo(dto.getIdade());
        assertThat(clienteUpdated.getEndereco()).isEqualTo(dto.getEndereco());
        assertThat(clienteUpdated.getNumero()).isEqualTo(dto.getNumero());
        assertThat(clienteUpdated.getCidade()).isEqualTo(dto.getCidade());
        assertThat(clienteUpdated.getUf()).isEqualTo(UnidadeFederacao.fromSigla(dto.getUf().toString()));
        assertThat(clienteUpdated.getCpf()).isEqualTo(dto.getCpf());
        assertThat(clienteUpdated.getTelefone()).isEqualTo(dto.getTelefone());
        assertThat(clienteUpdated.getEmail()).isEqualTo(OUTRO_EMAIL);
    }

}

