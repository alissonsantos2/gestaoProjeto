package br.com.microsoft.gestao.gestao_projetos_backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CadastroClienteRetornoDto {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
}
