package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.ProjetoRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroProjetoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Status;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroCliente;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroProjeto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CadastroProjetoImpl implements CadastroProjeto {

    Logger LOG = LoggerFactory.getLogger(CadastroProjetoImpl.class);
    private Validator validador;
    private CadastroCliente cadastroCliente;
    @Autowired
    private ProjetoRepository repository;

    public CadastroProjetoImpl(ProjetoRepository repository, Validator validador, CadastroCliente cadastroCliente) {
        this.repository = repository;
        this.validador = validador;
        this.cadastroCliente = cadastroCliente;
    }

    @Override
    @Transactional
    public CadastroProjetoRetornoDto cadastrar(CadastroProjetoDto cadastroProjetoDto, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Cadastrando projeto - {}", idTransacao);
        Set<ConstraintViolation<CadastroProjetoDto>> violations = validador.validate(cadastroProjetoDto);

        if (violations.isEmpty()) {
            Projeto projetoSalvo = repository.save(validarDadosMontarCadastroProjeto(cadastroProjetoDto, idTransacao));
            return new ModelMapper().map(projetoSalvo, CadastroProjetoRetornoDto.class);
        }
        LOG.info("Projeto nao validado - {}", idTransacao);
        throw new ExcecaoNegocio(violations.stream().findFirst().get().getMessage());
    }

    @Override
    @Transactional
    public Projeto buscarProjeto(Long id, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Buscando Projeto por id - {}", idTransacao);
        Optional<Projeto> projeto = repository.findById(id);
        if (projeto.isPresent()) {
            return projeto.get();
        } else {
            LOG.info("Projeto não existe - {}", idTransacao);
            throw new ExcecaoNegocio("Projeto informado não existe");
        }
    }

    @Override
    @Transactional
    public Projeto buscarProjetoPorCliente(Cliente cliente, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Buscando Projeto por cliente - {}", idTransacao);
        Optional<Projeto> projeto = repository.findByCliente(cliente);
        if (projeto.isPresent()) {
            return projeto.get();
        } else {
            LOG.info("Projeto não existe - {}", idTransacao);
            throw new ExcecaoNegocio("Projeto informado não existe");
        }
    }

    @Override
    @Transactional
    public CadastroProjetoRetornoDto vincular(CadastroProjetoDto cadastroProjetoDto, Long idProjeto, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Vinculando cliente ao projeto - {}", idTransacao);

        Projeto projeto = repository.findById(idProjeto).orElseThrow(() -> new ExcecaoNegocio("Esse projeto não existe"));

        if (!Status.EM_ABERTO.getStatus().equals(projeto.getStatus())) {
            throw new ExcecaoNegocio("Esse projeto já está vinculado a um cliente");
        }

        Cliente cliente = cadastroCliente.buscarCliente(cadastroProjetoDto.getIdCliente(), idTransacao);
        if (cliente == null) {
            throw new ExcecaoNegocio("Cliente não encontrado");
        }

        LOG.info("Atualizando projeto com cliente id: {} e status: {}", cliente.getId(), Status.EM_ANDAMENTO.getStatus());

        projeto.setCliente(cliente);
        projeto.setStatus(Status.EM_ANDAMENTO.getStatus());

        try {
            Projeto projetoSalvo = repository.save(projeto);
            return new ModelMapper().map(projetoSalvo, CadastroProjetoRetornoDto.class);
        } catch (Exception e) {
            LOG.error("Erro ao salvar o projeto: {}", e.getMessage(), e);
            throw new ExcecaoNegocio("Erro ao salvar o projeto");
        }
    }

    @Override
    public List<CadastroProjetoRetornoDto> buscarProjetosEmAberto(String idTransacao) {
        LOG.info("Buscando projetos em aberto - {}", idTransacao);
        List<Projeto> lsProjetos = repository.findByStatusEquals(Status.EM_ABERTO.getStatus());

        List<CadastroProjetoRetornoDto> lsProjetosDto = lsProjetos.stream()
                .map(projeto -> new ModelMapper().map(projeto, CadastroProjetoRetornoDto.class))
                .collect(Collectors.toList());

        return lsProjetosDto;
    }

    private Projeto validarDadosMontarCadastroProjeto(CadastroProjetoDto cadastroProjetoDto, String idTransacao) throws ExcecaoNegocio {
        Projeto projeto = new Projeto();
        Optional<Projeto> busca = repository.findByNome(cadastroProjetoDto.getNome());
        if (!busca.isPresent()) {
            projeto.setNome(cadastroProjetoDto.getNome());
            projeto.setStatus(Status.EM_ABERTO.getStatus());
            if (!cadastroProjetoDto.getDescricao().isEmpty()) {
                projeto.setDescricao(cadastroProjetoDto.getDescricao());
            }
            return projeto;
        }
        LOG.info("Esse projeto já existe - {}", idTransacao);
        throw new ExcecaoNegocio("Esse projeto já existe");
    }
}
