package br.com.microsoft.gestao.gestao_projetos_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CadastroProjetoDto {

    private Long idProjeto;

    @NotNull(message = "Informe o nome")
    private String nome;

    private String descricao;

    private Long idCliente;


}
