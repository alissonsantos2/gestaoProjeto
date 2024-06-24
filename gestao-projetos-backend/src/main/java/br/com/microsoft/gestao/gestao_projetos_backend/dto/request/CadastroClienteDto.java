package br.com.microsoft.gestao.gestao_projetos_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CadastroClienteDto {

    @NotNull(message = "Informe o nome")
    private String nome;

    @NotNull(message = "Informe o endereço")
    private String endereco;

    @NotNull(message = "Informe o telefone")
    private String telefone;
}
