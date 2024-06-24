package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.ClienteRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroClienteDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroClienteRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CadastroClienteImplTest {

    @Mock
    private Validator validador;

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private CadastroClienteImpl cadastroClienteImpl;
    @Mock
    private ModelMapper modelMapper;
    private CadastroClienteDto cadastroClienteDto;
    @Mock
    private CadastroCliente cadastroCliente;
    private static final String ID_TRANSACAO = "1234567890";

    @BeforeEach
    public void setUp() throws ExcecaoNegocio {
        cadastroClienteDto = new CadastroClienteDto();
        cadastroClienteDto.setNome("Teste 1");
        cadastroClienteDto.setEndereco("Endereço teste 1");
        cadastroClienteDto.setTelefone("79965539878");

        when(validador.validate(any(CadastroAtividadeDto.class))).thenReturn(Collections.emptySet());
    }

    @Test
    public void testCadastrarClienteValida() throws ExcecaoNegocio {
        Cliente cliente = new Cliente();
        CadastroAtividadeRetornoDto retornoDto = new CadastroAtividadeRetornoDto();
        retornoDto.setId(1L);

        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        when(modelMapper.map(cliente, CadastroAtividadeRetornoDto.class)).thenReturn(retornoDto);

        CadastroClienteRetornoDto resultado = cadastroClienteImpl.cadastrar(cadastroClienteDto, ID_TRANSACAO);

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testCadastrarClienteJaExiste() {
        Cliente clienteExistente = new Cliente();

        when(repository.findByNome(cadastroClienteDto.getNome())).thenReturn(Optional.of(clienteExistente));

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroClienteImpl.cadastrar(cadastroClienteDto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Esse cliente já existe", thrown.getMessage());
    }

    @Test
    public void testCadastrarClienteInvalida() {
        ConstraintViolation<CadastroClienteDto> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Erro de validação");
        Set<ConstraintViolation<CadastroClienteDto>> violations = new HashSet<>();
        violations.add(violation);

        when(validador.validate(cadastroClienteDto)).thenReturn(violations);

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroClienteImpl.cadastrar(cadastroClienteDto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Erro de validação", thrown.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    public void testBuscaCliente() throws ExcecaoNegocio {
        Long idCliente = 1L;
        Cliente cliente = new Cliente();
        cliente.setNome("Nome 1");

        when(repository.findById(idCliente)).thenReturn(Optional.of(cliente));

        Cliente resultado = cadastroClienteImpl.buscarCliente(idCliente, ID_TRANSACAO);

        assertNotNull(resultado);
        assertEquals("Nome 1", resultado.getNome());
    }

    @Test
    public void testBuscarClienteInexistente() {
        Long idCliente = 1L;

        when(repository.findById(idCliente)).thenReturn(Optional.empty());

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroClienteImpl.buscarCliente(idCliente, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Clientes informado não existe", thrown.getMessage());
        verify(repository, times(1)).findById(idCliente);
    }
}