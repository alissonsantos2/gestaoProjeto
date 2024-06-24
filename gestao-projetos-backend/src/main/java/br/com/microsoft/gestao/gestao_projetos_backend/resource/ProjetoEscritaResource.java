package br.com.microsoft.gestao.gestao_projetos_backend.resource;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroProjetoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.GestaoProjetoResource;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.RestResponseDTO;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroProjeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/projeto")
public class ProjetoEscritaResource extends GestaoProjetoResource {


    private final CadastroProjeto projeto;

    @Autowired
    public ProjetoEscritaResource(CadastroProjeto projeto) {
        this.projeto = projeto;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<RestResponseDTO<CadastroProjetoRetornoDto>> cadastrar(
            @RequestHeader(name = "idTransacao", required = false) String idTransacao,
            @RequestBody @Valid CadastroProjetoDto cadastroProjetoDto) throws ExcecaoNegocio {
        return retornarSucesso(projeto.cadastrar(cadastroProjetoDto, idTransacao));
    }

    @PutMapping("/vincular/{idProjeto}")
    public ResponseEntity<RestResponseDTO<CadastroProjetoRetornoDto>> vincularSetorFuncao(
            @PathVariable(name = "idProjeto") Long idProjeto,
            @RequestBody CadastroProjetoDto cadastroProjetoDto,
            @RequestHeader(name = "idTransacao", required = false) String idTransacao) throws ExcecaoNegocio {

        return retornarSucesso(projeto.vincular(cadastroProjetoDto,idProjeto, idTransacao));
    }
}
