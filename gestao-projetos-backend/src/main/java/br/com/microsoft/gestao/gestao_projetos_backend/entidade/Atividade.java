package br.com.microsoft.gestao.gestao_projetos_backend.entidade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nm_nome", length = 45)
    private String nome;

    @Column(name = "nm_descricao", length = 90)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "fk_projeto", foreignKey = @ForeignKey(name = "fk_projeto_atividade"))
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", foreignKey = @ForeignKey(name = "fk_cliente_atividade"))
    private Cliente cliente;
}
