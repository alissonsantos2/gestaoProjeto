package br.com.microsoft.gestao.gestao_projetos_backend.entidade;

public class ExcecaoNegocio extends Exception{
    public ExcecaoNegocio(String mensagem) {
        super(mensagem);
    }
}
