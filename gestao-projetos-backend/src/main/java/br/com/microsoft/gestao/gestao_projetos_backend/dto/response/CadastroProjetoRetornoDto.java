package br.com.microsoft.gestao.gestao_projetos_backend.dto.response;

import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroProjetoRetornoDto {

    private Long id;
    private String nome;
    private String descricao;
    private String status;
    private Cliente cliente;
}
