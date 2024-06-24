package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.AtividadeRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Atividade;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroAtividade;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
public class CadastroAtividadeImpl implements CadastroAtividade {

    Logger LOG = LoggerFactory.getLogger(CadastroAtividadeImpl.class);
    private Validator validador;
    private CadastroCliente cadastroCliente;
    private CadastroProjeto cadastroProjeto;
    @Autowired
    private AtividadeRepository repository;

    public CadastroAtividadeImpl(AtividadeRepository repository, Validator validador, CadastroProjeto cadastroProjeto, CadastroCliente cadastroCliente) {
        this.repository = repository;
        this.validador = validador;
        this.cadastroProjeto = cadastroProjeto;
        this.cadastroCliente = cadastroCliente;
    }


    @Override
    @Transactional
    public CadastroAtividadeRetornoDto cadastrar(CadastroAtividadeDto cadastroAtividadeDto, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Cadastrando atividade - {}", idTransacao);
        Set<ConstraintViolation<CadastroAtividadeDto>> violations = validador.validate(cadastroAtividadeDto);

        if (violations.isEmpty()) {
            Atividade atividadeSalvo = repository.save(validarDadosMontarCadastroAtividade(cadastroAtividadeDto, idTransacao));
            return new ModelMapper().map(atividadeSalvo, CadastroAtividadeRetornoDto.class);
        }
        LOG.info("Atividade nao validado - {}", idTransacao);
        throw new ExcecaoNegocio(violations.stream().findFirst().get().getMessage());
    }

    @Override
    public List<Atividade> buscaAtividadesPorProjeto(Long idProjeto, String idTransacao) throws ExcecaoNegocio {

        List<Atividade> lsAtividades = repository.findByProjeto(cadastroProjeto.buscarProjeto(idProjeto, idTransacao));
        return lsAtividades;
    }

    private Atividade validarDadosMontarCadastroAtividade(CadastroAtividadeDto cadastroAtividadeDto, String idTransacao) throws ExcecaoNegocio {
        Atividade atividade = new Atividade();
        Optional<Atividade> busca = repository.findByNome(cadastroAtividadeDto.getNome());
        if (!busca.isPresent()) {
            atividade.setNome(cadastroAtividadeDto.getNome());
            if (!cadastroAtividadeDto.getDescricao().isEmpty()) {
                atividade.setDescricao(cadastroAtividadeDto.getDescricao());
            }
            Cliente cliente = cadastroCliente.buscarCliente(cadastroAtividadeDto.getIdCliente(), idTransacao);
            Projeto projeto = cadastroProjeto.buscarProjetoPorCliente(cliente, idTransacao);
            if (Objects.nonNull(projeto)) {
                atividade.setProjeto(projeto);
                atividade.setCliente(cadastroCliente.buscarCliente(cadastroAtividadeDto.getIdCliente(), idTransacao));
            }
            return atividade;
        }
        LOG.info("Essa atividade já existe - {}", idTransacao);
        throw new ExcecaoNegocio("Essa atividade já existe");
    }
}
