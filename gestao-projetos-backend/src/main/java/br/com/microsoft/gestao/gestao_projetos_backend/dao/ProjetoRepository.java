package br.com.microsoft.gestao.gestao_projetos_backend.dao;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNome(String nome);
    Optional<Projeto> findByCliente(Cliente cliente);
    Optional<Projeto> findByIdAndStatusEquals (Long idProjeto, String status);

    List<Projeto> findByStatusEquals(String status);

}
