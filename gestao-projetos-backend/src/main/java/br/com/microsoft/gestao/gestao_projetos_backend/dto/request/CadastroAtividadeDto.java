package br.com.microsoft.gestao.gestao_projetos_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CadastroAtividadeDto {

    @NotNull(message = "Informe o nome")
    private String nome;

    @NotNull(message = "Informe a descricao")
    private String descricao;

    @NotNull(message = "Informe o cliente")
    private Long idCliente;

    @NotNull(message = "Informe o projeto")
    private Long idProjeto;

}
