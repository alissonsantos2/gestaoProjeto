package br.com.microsoft.gestao.gestao_projetos_backend.service;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroClienteDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroClienteRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Atividade;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;

import java.util.List;

public interface CadastroAtividade {

    CadastroAtividadeRetornoDto cadastrar(CadastroAtividadeDto cadastroAtividadeDto, String idTransacao) throws ExcecaoNegocio;

    List<Atividade> buscaAtividadesPorProjeto(Long idProjeto, String idTransacao) throws ExcecaoNegocio;
}
