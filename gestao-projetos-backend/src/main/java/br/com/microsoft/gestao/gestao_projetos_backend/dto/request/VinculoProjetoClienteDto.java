package br.com.microsoft.gestao.gestao_projetos_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class VinculoProjetoClienteDto {

    @NotNull(message = "Um projeto precisa ser escolhido")
    @Positive(message = "Um projeto precisa ser escolhido")
    private Long idProjeto;

    @NotNull(message = "Um cliente precisa ser escolhido")
    @Positive(message = "Um cliente precisa ser escolhido")
    private Long idCliente;
}
