package br.com.microsoft.gestao.gestao_projetos_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class GestaoProjetosBackendApplication {

	public static final String CONTAINER_ID = UUID.randomUUID().toString();
	public static void main(String[] args) {
		SpringApplication.run(GestaoProjetosBackendApplication.class, args);
	}

}
