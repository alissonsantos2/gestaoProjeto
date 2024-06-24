package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.ProjetoRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroProjetoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Status;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroProjeto;
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
public class CadastroProjetoImplTest {

    @Mock
    private Validator validador;

    @Mock
    private ProjetoRepository repository;

    @InjectMocks
    private CadastroProjetoImpl cadastroProjetoImpl;
    @Mock
    private ModelMapper modelMapper;
    private CadastroProjetoDto cadastroProjetoDto;
    private Projeto retornoProjeto1;
    private Projeto retornoProjeto2;
    @Mock
    private CadastroProjeto cadastroProjeto;
    private static final String ID_TRANSACAO = "1234567890";

    @Mock
    private CadastroClienteImpl cadastroCliente;

    @BeforeEach
    public void setUp() {
        cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setNome("Teste 1");
        cadastroProjetoDto.setDescricao("Descrição teste 1");

        retornoProjeto1 = new Projeto();
        retornoProjeto1.setId(1L);
        retornoProjeto1.setNome("Projeto 1");
        retornoProjeto1.setDescricao("Descrição 1");

        retornoProjeto2 = new Projeto();
        retornoProjeto2.setId(2L);
        retornoProjeto2.setNome("Projeto 2");
        retornoProjeto2.setDescricao("Descrição 2");

        when(validador.validate(any(CadastroProjetoDto.class))).thenReturn(Collections.emptySet());
    }

    @Test
    public void testCadastrarProjetoValida() throws ExcecaoNegocio {
        Projeto projeto = new Projeto();
        CadastroProjetoRetornoDto retornoDto = new CadastroProjetoRetornoDto();
        retornoDto.setId(1L);

        when(repository.save(any(Projeto.class))).thenReturn(projeto);
        when(repository.findById(1L)).thenReturn(Optional.of(projeto));
        when(modelMapper.map(projeto, CadastroProjetoRetornoDto.class)).thenReturn(retornoDto);

        CadastroProjetoRetornoDto resultado = cadastroProjetoImpl.cadastrar(cadastroProjetoDto, ID_TRANSACAO);

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(Projeto.class));
    }

    @Test
    public void testCadastrarProjetoJaExiste() {
        Projeto projetoExistente = new Projeto();

        when(repository.findByNome(cadastroProjetoDto.getNome())).thenReturn(Optional.of(projetoExistente));

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.cadastrar(cadastroProjetoDto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Esse projeto já existe", thrown.getMessage());
    }

    @Test
    public void testCadastrarProjetoInvalida() {
        ConstraintViolation<CadastroProjetoDto> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Erro de validação");
        Set<ConstraintViolation<CadastroProjetoDto>> violations = new HashSet<>();
        violations.add(violation);

        when(validador.validate(cadastroProjetoDto)).thenReturn(violations);

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.cadastrar(cadastroProjetoDto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Erro de validação", thrown.getMessage());
        verify(repository, never()).save(any(Projeto.class));
    }

    @Test
    public void testBuscaProjeto() throws ExcecaoNegocio {
        Long idProjeto = 1L;
        Projeto projeto = new Projeto();
        projeto.setNome("Nome 1");

        when(repository.findById(idProjeto)).thenReturn(Optional.of(projeto));

        Projeto resultado = cadastroProjetoImpl.buscarProjeto(idProjeto, ID_TRANSACAO);

        assertNotNull(resultado);
        assertEquals("Nome 1", resultado.getNome());
    }

    @Test
    public void testBuscarProjetoInexistente() {
        Long idProjeto = 1L;

        when(repository.findById(idProjeto)).thenReturn(Optional.empty());

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.buscarProjeto(idProjeto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Projeto informado não existe", thrown.getMessage());
    }

    @Test
    public void testBuscarProjetosEmAberto() {
        List<Projeto> projetosEmAberto = Arrays.asList(retornoProjeto1, retornoProjeto2);

        when(repository.findByStatusEquals(Status.EM_ABERTO.getStatus())).thenReturn(projetosEmAberto);

        List<CadastroProjetoRetornoDto> resultado = cadastroProjetoImpl.buscarProjetosEmAberto(ID_TRANSACAO);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Projeto 1", resultado.get(0).getNome());
        assertEquals("Projeto 2", resultado.get(1).getNome());

        verify(repository, times(1)).findByStatusEquals(Status.EM_ABERTO.getStatus());
    }

    @Test
    public void testVincularProjetoComSucesso() throws ExcecaoNegocio {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(Status.EM_ABERTO.getStatus());

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(projeto));
        when(cadastroCliente.buscarCliente(1L, ID_TRANSACAO)).thenReturn(cliente);
        when(repository.save(any(Projeto.class))).thenReturn(projeto);

        CadastroProjetoRetornoDto resultado = cadastroProjetoImpl.vincular(cadastroProjetoDto, 1l,ID_TRANSACAO);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(projeto);
    }

    @Test
    public void testVincularProjetoJaVinculado() {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(Status.EM_ANDAMENTO.getStatus());

        when(repository.findById(1L)).thenReturn(Optional.of(projeto));

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.vincular(cadastroProjetoDto, 1l, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Esse projeto já está vinculado a um cliente", thrown.getMessage());
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(Projeto.class));
    }

    @Test
    public void testVincularProjetoComViolacaoDeValidacao() {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        ConstraintViolation<CadastroProjetoDto> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Erro de validação");

        Set<ConstraintViolation<CadastroProjetoDto>> violations = new HashSet<>();
        violations.add(violation);

        when(validador.validate(cadastroProjetoDto)).thenReturn(violations);

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.vincular(cadastroProjetoDto, 1l, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Esse projeto não existe", thrown.getMessage());
        verify(repository, never()).save(any(Projeto.class));
    }

    @Test
    public void testVincularProjetoNaoExiste() {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.vincular(cadastroProjetoDto, 1l, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Esse projeto não existe", thrown.getMessage());
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(Projeto.class));
    }

    @Test
    public void testBuscarProjetoPorClienteExistente() throws ExcecaoNegocio {
        Cliente cliente = new Cliente();
        cliente.setId(1l);
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");

        when(repository.findByCliente(cliente)).thenReturn(Optional.of(projeto));

        Projeto resultado = cadastroProjetoImpl.buscarProjetoPorCliente(cliente, ID_TRANSACAO);

        assertNotNull(resultado);
        assertEquals("Projeto Teste", resultado.getNome());
        verify(repository, times(1)).findByCliente(cliente);
    }

    @Test
    public void testBuscarProjetoPorClienteNaoExistente() {
        Cliente cliente = new Cliente();
        cliente.setId(1l);

        when(repository.findByCliente(cliente)).thenReturn(Optional.empty());

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.buscarProjetoPorCliente(cliente, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Projeto informado não existe", thrown.getMessage());
        verify(repository, times(1)).findByCliente(cliente);
    }

    @Test
    public void testVincularClienteNaoEncontrado() throws ExcecaoNegocio {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(Status.EM_ABERTO.getStatus());

        when(repository.findById(anyLong())).thenReturn(Optional.of(projeto));
        when(cadastroCliente.buscarCliente(anyLong(), anyString())).thenReturn(null);

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.vincular(cadastroProjetoDto, 1L, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, never()).save(any(Projeto.class));
    }
    @Test
    public void testVincularErroAoSalvarProjeto() throws ExcecaoNegocio {
        CadastroProjetoDto cadastroProjetoDto = new CadastroProjetoDto();
        cadastroProjetoDto.setIdProjeto(1L);
        cadastroProjetoDto.setIdCliente(1L);

        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(Status.EM_ABERTO.getStatus());

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(repository.findById(anyLong())).thenReturn(Optional.of(projeto));
        when(cadastroCliente.buscarCliente(anyLong(), anyString())).thenReturn(cliente);
        when(repository.save(any(Projeto.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroProjetoImpl.vincular(cadastroProjetoDto, 1L, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Erro ao salvar o projeto", thrown.getMessage());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any(Projeto.class));
    }
}