package br.com.microsoft.gestao.gestao_projetos_backend.service;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroProjetoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;

import java.util.List;

public interface CadastroProjeto {

    CadastroProjetoRetornoDto cadastrar(CadastroProjetoDto cadastroProjetoDto, String idTransacao) throws ExcecaoNegocio;
    Projeto buscarProjeto(Long id, String idTransacao)  throws ExcecaoNegocio;

    Projeto buscarProjetoPorCliente(Cliente cliente, String idTransacao)  throws ExcecaoNegocio;

    CadastroProjetoRetornoDto vincular(CadastroProjetoDto cadastroProjetoDto, Long idProjeto, String idTransacao) throws ExcecaoNegocio;

    List<CadastroProjetoRetornoDto> buscarProjetosEmAberto(String idTransacao);

}
