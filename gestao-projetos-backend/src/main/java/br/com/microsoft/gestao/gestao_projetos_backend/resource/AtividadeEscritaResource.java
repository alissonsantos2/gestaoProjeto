package br.com.microsoft.gestao.gestao_projetos_backend.resource;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.GestaoProjetoResource;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.RestResponseDTO;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroAtividade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/atividade")
public class AtividadeEscritaResource extends GestaoProjetoResource {


    private final CadastroAtividade atividade;

    @Autowired
    public AtividadeEscritaResource(CadastroAtividade atividade) {
        this.atividade = atividade;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<RestResponseDTO<CadastroAtividadeRetornoDto>> cadastrar(
            @RequestHeader(name = "idTransacao", required = false) String idTransacao,
            @RequestBody @Valid CadastroAtividadeDto cadastroAtividadeDto) throws ExcecaoNegocio {
        return retornarSucesso(atividade.cadastrar(cadastroAtividadeDto, idTransacao));
    }
}
