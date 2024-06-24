package br.com.microsoft.gestao.gestao_projetos_backend.dao;

import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByNome(String nome);

}
