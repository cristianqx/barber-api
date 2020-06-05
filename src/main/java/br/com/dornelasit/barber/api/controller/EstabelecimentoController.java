package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.services.EstabelecimentoService;
import br.com.dornelasitapi.barber.controller.AppEstabelecimentoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class EstabelecimentoController implements AppEstabelecimentoApi{
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	@Override
	public ResponseEntity<Void> manterEstabelecimento(@Valid EstabelecimentoResource estabelecimentoResource) {
		try {
			
			estabelecimentoService.cadastrarEstabelecimento(estabelecimentoResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			log.error("Erro ao cadastrar estabelecimento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<EstabelecimentoResource> obterEstabelecimentoById(Integer idEstabelecimento) {
		
		try {
			
			log.info("Requisicao de estabelecimento: " + idEstabelecimento);
			
			return this.estabelecimentoService.obterEstabelecimentoById(idEstabelecimento)
					.map(estabelecimento -> ResponseEntity.ok().body(estabelecimento))
					.orElse(ResponseEntity.notFound().build());

		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			log.info("Erro ao buscar estabelecimento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<EstabelecimentoResource>> obterListaEstabelecimentos() {

		try {
			final List<EstabelecimentoResource> estabelecimentoRetorno = estabelecimentoService.buscarListaEstabelecimentos();
			
			return new ResponseEntity<List<EstabelecimentoResource>>(estabelecimentoRetorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro ao retornar lista de estabelecimentos: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
