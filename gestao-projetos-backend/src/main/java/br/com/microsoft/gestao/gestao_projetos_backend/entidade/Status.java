package br.com.microsoft.gestao.gestao_projetos_backend.entidade;

import lombok.Getter;

@Getter
public enum Status {
    EM_ABERTO("aberto"),
    EM_ANDAMENTO("andamento");

    private String status;
    private Status(String status){
        this.status = status;
    }

}
