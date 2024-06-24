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
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nm_nome", length = 45)
    private String nome;

    @Column(name = "nm_descricao", length = 90)
    private String descricao;

    @Column(name = "nm_status", length = 9)
    private String status;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", foreignKey = @ForeignKey(name = "fk_cliente_projeto"))
    private Cliente cliente;

}
