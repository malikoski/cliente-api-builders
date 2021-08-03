package br.com.builders.cliente.integration;

import br.com.builders.cliente.helper.ClienteTestHelper;
import br.com.builders.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ClienteOperationsListTest {

    static final String CLIENTES_API = "/clientes";
    public static final String CIDADE_CURITIBA = "Curitiba";
    public static final String NOME_MARCOS_SILVA = "Marcos Silva";
    public static final String NOME_MARCOS_PEDRO = "Marcos Pedro";
    private static final String CIDADE_CASCAVEL = "Cascavel";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    public void findAllWithoutFilter() throws Exception {

        var clientes = ClienteTestHelper.createListClientes();

        clientes.forEach(c -> clienteRepository.save(c));

        assertThat(clienteRepository.findAll()).isNotNull();

        mockMvc.perform(
                get(CLIENTES_API + "?page=0&size=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isArray())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("pageable.page").value(0))
                .andExpect(jsonPath("pageable.size").value(clientes.size()))
                .andExpect(jsonPath("totalElements").value(clientes.size()));
    }

    @Test
    @DisplayName("Deve listar Clientes baseados com um filtro de cidade")
    public void findAllWithFilter() throws Exception {

        var clientes = ClienteTestHelper.createListClientes();

        clientes.get(3).setCidade(CIDADE_CURITIBA);
        clientes.get(4).setCidade(CIDADE_CURITIBA);

        clientes.forEach(c -> clienteRepository.save(c));

        assertThat(clienteRepository.findAll()).isNotNull();


        mockMvc.perform(
                get(CLIENTES_API + "?page=0&size=20&search=cidade:Curitiba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isArray())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("pageable.page").value(0))
                .andExpect(jsonPath("pageable.size").value(clientes.size()))
                .andExpect(jsonPath("totalElements").value(2));
    }


    @Test
    @DisplayName("Deve listar Clientes baseados com múltiplos filtros")
    public void findAllWithMultipeFilters() throws Exception {

        var clientes = ClienteTestHelper.createListClientes();

        clientes.get(10).setNome(NOME_MARCOS_SILVA);
        clientes.get(11).setNome(NOME_MARCOS_PEDRO);
        clientes.get(11).setCidade(CIDADE_CASCAVEL);

        clientes.forEach(c -> clienteRepository.save(c));

        assertThat(clienteRepository.findAll()).isNotNull();

        mockMvc.perform(
                get(CLIENTES_API + "?page=0&size=20&search=nome~Marcos,cidade:Cascavel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isArray())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("pageable.page").value(0))
                .andExpect(jsonPath("pageable.size").value(clientes.size()))
                .andExpect(jsonPath("totalElements").value(1));
    }
}

