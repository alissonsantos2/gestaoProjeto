package br.com.microsoft.gestao.gestao_projetos_backend.resource;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroAtividadeDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroAtividadeRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.GestaoProjetoResource;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.RestResponseDTO;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.Atividade;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroAtividade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/atividade")
public class AtividadeLeituraResource extends GestaoProjetoResource {


    private final CadastroAtividade atividade;

    @Autowired
    public AtividadeLeituraResource(CadastroAtividade atividade) {
        this.atividade = atividade;
    }

    @GetMapping("/{idProjeto}")
    public ResponseEntity<RestResponseDTO<List<Atividade>>> getAtividadesPorProjeto(
            @PathVariable(name = "idProjeto") Long idProjeto,
            @RequestHeader(name = "idTransacao", required = false) String idTransacao) throws ExcecaoNegocio {
        return retornarSucesso(atividade.buscaAtividadesPorProjeto(idProjeto, idTransacao));
    }
}
