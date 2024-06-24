package br.com.microsoft.gestao.gestao_projetos_backend.dao;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Atividade;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    Optional<Atividade> findByNome(String nome);

    List<Atividade> findByProjeto(Projeto projeto);
}
