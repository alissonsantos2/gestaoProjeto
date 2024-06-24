package br.com.microsoft.gestao.gestao_projetos_backend.dto.utils;

import br.com.microsoft.gestao.gestao_projetos_backend.GestaoProjetosBackendApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GestaoProjetoResource {
    private <T extends Object> ResponseEntity<RestResponseDTO<T>> retornarResponse(final T response){
        RestResponseDTO<T> restResponse = new RestResponseDTO<>();
        restResponse.setContainerId(GestaoProjetosBackendApplication.CONTAINER_ID);
        if (response == null || (response instanceof List<?> && ((List<?>)response).isEmpty())) {
            restResponse.setStatus(ResponseStatusEnum.SEM_RESULTADOS);
        } else {
            restResponse.setStatus(ResponseStatusEnum.OK);
        }
        restResponse.setResultado(response);
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    public <T extends Object> ResponseEntity<RestResponseDTO<T>> retornarSucesso(final T response) {
        return retornarResponse(response);
    }
}
