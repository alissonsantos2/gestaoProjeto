package br.com.microsoft.gestao.gestao_projetos_backend.service;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroClienteDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroClienteRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;

public interface CadastroCliente {

    CadastroClienteRetornoDto cadastrar(CadastroClienteDto cadastroClienteDto, String idTransacao) throws ExcecaoNegocio;
    Cliente buscarCliente(Long id, String idTransacao)  throws ExcecaoNegocio;
}
