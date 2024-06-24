package br.com.microsoft.gestao.gestao_projetos_backend.resource;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroProjetoRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.GestaoProjetoResource;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.RestResponseDTO;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroProjeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/projeto")
public class ProjetoLeituraResource extends GestaoProjetoResource {


    private final CadastroProjeto projeto;

    @Autowired
    public ProjetoLeituraResource(CadastroProjeto projeto) {
        this.projeto = projeto;
    }

    @GetMapping("/listarProjetosEmAberto")
    public ResponseEntity<RestResponseDTO<List<CadastroProjetoRetornoDto>>> getProjetosEmAberto(
            @RequestHeader(name = "idTransacao", required = false) String idTransacao) {

        return retornarSucesso(projeto.buscarProjetosEmAberto(idTransacao));
    }
}
