package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.ClienteRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroClienteDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroClienteRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroCliente;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;


@Service
public class CadastroClienteImpl implements CadastroCliente {

    Logger LOG = LoggerFactory.getLogger(CadastroClienteImpl.class);

    private Validator validador;
    @Autowired
    private ClienteRepository repository;

    public CadastroClienteImpl(ClienteRepository repository, Validator validador) {
        this.repository = repository;
        this.validador = validador;
    }

    @Override
    @Transactional
    public CadastroClienteRetornoDto cadastrar(CadastroClienteDto cadastroClienteDto, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Cadastrando cliente - {}", idTransacao);
        Set<ConstraintViolation<CadastroClienteDto>> violations = validador.validate(cadastroClienteDto);

        if (violations.isEmpty()) {
            Cliente clienteSalvo = repository.save(validarDadosMontarCadastroCliente(cadastroClienteDto, idTransacao));
            return new ModelMapper().map(clienteSalvo, CadastroClienteRetornoDto.class);
        }
        LOG.info("Cliente nao validado - {}", idTransacao);
        throw new ExcecaoNegocio(violations.stream().findFirst().get().getMessage());
    }

    @Override
    @Transactional
    public Cliente buscarCliente(Long id, String idTransacao) throws ExcecaoNegocio {
        LOG.info("Buscando Cliente por id - {}", idTransacao);
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isPresent()) {
            return cliente.get();
        } else {
            LOG.info("Cliente não existe - {}", idTransacao);
            throw new ExcecaoNegocio("Clientes informado não existe");
        }
    }

    private Cliente validarDadosMontarCadastroCliente(CadastroClienteDto cadastroClienteDto, String idTransacao) throws ExcecaoNegocio {
        Cliente cliente = new Cliente();
        Optional<Cliente> busca = repository.findByNome(cadastroClienteDto.getNome());
        if (!busca.isPresent()) {
            cliente.setNome(cadastroClienteDto.getNome());
            if (!cadastroClienteDto.getEndereco().isEmpty()) {
                cliente.setEndereco(cadastroClienteDto.getEndereco());
            }
            if (!cadastroClienteDto.getTelefone().isEmpty()) {
                cliente.setTelefone(cadastroClienteDto.getTelefone());
            }
            return cliente;
        }
        LOG.info("Cliente ja cadastrada - {}", idTransacao);
        throw new ExcecaoNegocio("Esse cliente já existe");
    }
}
