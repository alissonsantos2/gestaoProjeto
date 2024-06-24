package br.com.microsoft.gestao.gestao_projetos_backend.resource;

import br.com.microsoft.gestao.gestao_projetos_backend.dto.request.CadastroClienteDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.response.CadastroClienteRetornoDto;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.GestaoProjetoResource;
import br.com.microsoft.gestao.gestao_projetos_backend.dto.utils.RestResponseDTO;
import br.com.microsoft.gestao.gestao_projetos_backend.entidade.ExcecaoNegocio;
import br.com.microsoft.gestao.gestao_projetos_backend.service.CadastroCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/cliente")
public class ClienteEscritaResource extends GestaoProjetoResource {


    @Autowired
    public ClienteEscritaResource(CadastroCliente cliente) {
        this.cliente = cliente;
    }

    private final CadastroCliente cliente;

    @PostMapping("/cadastrar")
    public ResponseEntity<RestResponseDTO<CadastroClienteRetornoDto>> cadastrar(
            @RequestHeader(name = "idTransacao", required = false) String idTransacao,
            @RequestBody @Valid CadastroClienteDto cadastroClienteDto) throws ExcecaoNegocio {
        return retornarSucesso(cliente.cadastrar(cadastroClienteDto, idTransacao));
    }
}
