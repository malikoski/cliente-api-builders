package br.com.builders.cliente.resource;

import br.com.builders.cliente.helper.ClienteTestHelper;
import br.com.builders.cliente.resource.dto.ClienteRequest;
import br.com.builders.cliente.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ClienteResource.class})
public class ClienteResourceTest {

    static final String CLIENTES_API = "/clientes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    @DisplayName("Deve salvar um novo cliente")
    public void createNewCliente() throws Exception {

        var dto = ClienteTestHelper.createClientRequest();
        var savedCliente = ClienteTestHelper.createClienteResponse();

        BDDMockito.given(clienteService.save(any(ClienteRequest.class))).willReturn(savedCliente);

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(
                post(CLIENTES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("uuid").value(ClienteTestHelper.UUID.toString()))
                .andExpect(jsonPath("nome").value(dto.getNome()))
                .andExpect(jsonPath("idade").value(dto.getIdade()))
                .andExpect(jsonPath("endereco").value(dto.getEndereco()))
                .andExpect(jsonPath("numero").value(dto.getNumero()))
                .andExpect(jsonPath("cidade").value(dto.getCidade()))
                .andExpect(jsonPath("uf").value(dto.getUf().toString()))
                .andExpect(jsonPath("cpf").value(dto.getCpf()))
                .andExpect(jsonPath("telefone").value(dto.getTelefone()))
                .andExpect(jsonPath("email").value(dto.getEmail()));
    }

    @Test
    @DisplayName("Deve retornar retornar violações de campos inválidos")
    public void newClienteInvalido() throws Exception {

        var dto = ClienteTestHelper.createClientRequestInvalid();
        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(
                post(CLIENTES_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(8)));
    }

    @Test
    @DisplayName("Deve retornar um cliente por um UUID informado")
    public void findClienteByUuidTest() throws Exception {

        var clienteResponse = ClienteTestHelper.createClienteResponse();

        BDDMockito.given(clienteService.findByUuid(ClienteTestHelper.UUID)).willReturn(clienteResponse);

        mockMvc.perform(
                get(CLIENTES_API + "/" + ClienteTestHelper.UUID.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("uuid").value(ClienteTestHelper.UUID.toString()))
                .andExpect(jsonPath("nome").value(clienteResponse.getNome()))
                .andExpect(jsonPath("idade").value(clienteResponse.getIdade()))
                .andExpect(jsonPath("endereco").value(clienteResponse.getEndereco()))
                .andExpect(jsonPath("numero").value(clienteResponse.getNumero()))
                .andExpect(jsonPath("cidade").value(clienteResponse.getCidade()))
                .andExpect(jsonPath("uf").value(clienteResponse.getUf().toString()))
                .andExpect(jsonPath("cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("telefone").value(clienteResponse.getTelefone()))
                .andExpect(jsonPath("email").value(clienteResponse.getEmail()));
    }

    @Test
    @DisplayName("Deve retornar todos os clientes cadastrados paginados")
    public void getAllTest() throws Exception {

        var pageClienteResponse = ClienteTestHelper.createPageClienteResponse();

        BDDMockito.given(clienteService.findAll(any(Integer.class), any(Integer.class), any(Optional.class)))
                .willReturn(pageClienteResponse);

        mockMvc.perform(
                get(CLIENTES_API + "?page=1&size=20&search=idade>25,nome~Santos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(2))
                .andExpect(jsonPath("pageable.page").value(0))
                .andExpect(jsonPath("pageable.size").value(20));
    }


}

