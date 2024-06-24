package br.com.microsoft.gestao.gestao_projetos_backend.service.impl;

import br.com.microsoft.gestao.gestao_projetos_backend.dao.AtividadeRepository;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Atividade;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Cliente;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Projeto;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroCliente;
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
public class CadastroAtividadeImplTest {

    @Mock
    private Validator validador;

    @Mock
    private CadastroProjeto cadastroProjeto;

    @Mock
    private AtividadeRepository repository;

    @InjectMocks
    private CadastroAtividadeImpl cadastroAtividadeImpl;
    @Mock
    private ModelMapper modelMapper;
    private CadastroAtividadeDto cadastroAtividadeDto;
    @Mock
    private CadastroCliente cadastroCliente;
    private static final String ID_TRANSACAO = "1234567890";
    private Projeto projeto;
    private Cliente cliente;

    @BeforeEach
    public void setUp() throws ExcecaoNegocio {
        cadastroAtividadeDto = new CadastroAtividadeDto();
        cadastroAtividadeDto.setNome("Teste 1");
        cadastroAtividadeDto.setDescricao("Descrição teste 1");
        cadastroAtividadeDto.setIdProjeto(1L);
        cadastroAtividadeDto.setIdCliente(1L);

        projeto = new Projeto();
        cliente = new Cliente();
        cliente.setId(1L);
        projeto.setCliente(cliente);
        projeto.setNome("Projeto");
        projeto.setDescricao("Descrição");

        when(validador.validate(any(CadastroAtividadeDto.class))).thenReturn(Collections.emptySet());
        when(cadastroProjeto.buscarProjetoPorCliente(any(), anyString())).thenReturn(projeto);
        when(cadastroCliente.buscarCliente(anyLong(), anyString())).thenReturn(cliente);
    }

    @Test
    public void testCadastrarAtividadeValida() throws ExcecaoNegocio {
        Atividade atividade = new Atividade();
        CadastroAtividadeRetornoDto retornoDto = new CadastroAtividadeRetornoDto();
        retornoDto.setId(1L);

        when(repository.save(any(Atividade.class))).thenReturn(atividade);
        when(modelMapper.map(atividade, CadastroAtividadeRetornoDto.class)).thenReturn(retornoDto);

        CadastroAtividadeRetornoDto resultado = cadastroAtividadeImpl.cadastrar(cadastroAtividadeDto, ID_TRANSACAO);

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(Atividade.class));
    }

    @Test
    public void testCadastrarAtividadeJaExiste() {
        Atividade atividadeExistente = new Atividade();

        when(repository.findByNome(cadastroAtividadeDto.getNome())).thenReturn(Optional.of(atividadeExistente));

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroAtividadeImpl.cadastrar(cadastroAtividadeDto, ID_TRANSACAO);
        });
        assertNotNull(thrown);
        assertEquals("Essa atividade já existe", thrown.getMessage());
    }

    @Test
    public void testCadastrarAtividadeInvalida() {
        ConstraintViolation<CadastroAtividadeDto> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Erro de validação");
        Set<ConstraintViolation<CadastroAtividadeDto>> violations = new HashSet<>();
        violations.add(violation);

        when(validador.validate(cadastroAtividadeDto)).thenReturn(violations);

        ExcecaoNegocio thrown = assertThrows(ExcecaoNegocio.class, () -> {
            cadastroAtividadeImpl.cadastrar(cadastroAtividadeDto, ID_TRANSACAO);
        });

        assertNotNull(thrown);
        assertEquals("Erro de validação", thrown.getMessage());
        verify(repository, never()).save(any(Atividade.class));
    }

    @Test
    public void testBuscaAtividadesPorProjeto() throws ExcecaoNegocio {
        Long idProjeto = 1L;
        Atividade atividade = new Atividade();
        atividade.setNome("Atividade 1");
        List<Atividade> atividades = Collections.singletonList(atividade);

        when(repository.findByProjeto(cadastroProjeto.buscarProjeto(idProjeto,ID_TRANSACAO))).thenReturn(atividades);

        List<Atividade> resultado = cadastroAtividadeImpl.buscaAtividadesPorProjeto(idProjeto, ID_TRANSACAO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Atividade 1", resultado.get(0).getNome());
    }

}