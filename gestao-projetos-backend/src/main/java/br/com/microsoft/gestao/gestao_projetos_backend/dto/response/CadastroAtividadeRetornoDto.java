package br.com.microsoft.gestao.gestao_projetos_backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CadastroAtividadeRetornoDto {
    private Long id;
    private String nome;
    private String descricao;
}
