package br.com.microsoft.gestao.gestao_projetos_backend.dto.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponseDTO<T> {
    private ResponseStatusEnum status;
    private transient T resultado;
    private String containerId;

    public RestResponseDTO(){
        super();
    }

    public RestResponseDTO(final ResponseStatusEnum response, final T dados){
        super();
        this.status = response;
        this.resultado = dados;
    }
}
