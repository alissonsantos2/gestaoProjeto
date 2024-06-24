package br.com.microsoft.gestao.gestao_projetos_backend.entidade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nm_nome", length = 45)
    private String nome;

    @Column(name = "nm_endereco", length = 90)
    private String endereco;

    @Column(name = "nm_telefone", length = 10)
    private String telefone;

}
