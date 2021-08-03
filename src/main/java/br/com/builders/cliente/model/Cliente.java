package br.com.builders.cliente.model;

import br.com.builders.cliente.validator.OnCreate;
import br.com.builders.cliente.validator.OnUpdate;
import br.com.builders.cliente.validator.UniqueCPF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table
@Builder()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 601987832749038991L;

    @Id
    @SequenceGenerator(name="cliente_id_seq", sequenceName="cliente_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cliente_id_seq")
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "id", updatable=false)
    private Integer id;

    @Column(name = "uuid", updatable=false,  columnDefinition = "VARCHAR(255)")
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String nome;

    @UniqueCPF(groups = OnCreate.class)
    private String cpf;

    private Integer idade;

    private String endereco;

    @Column(name = "endereco_numero")
    private String numero;

    private String cep;

    private String cidade;

    private UnidadeFederacao uf;

    private String email;

    private String telefone;

    @PrePersist
    public void autofill() {
        this.setUuid(UUID.randomUUID());
    }
}
