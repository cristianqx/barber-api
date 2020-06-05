package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.ProfissionalException;
import br.com.dornelasit.barber.api.model.ProfissionalResource;
import br.com.dornelasit.barber.api.services.ProfissionalService;
import br.com.dornelasitapi.barber.controller.AppProfissionalApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class ProfissionalController implements AppProfissionalApi{

	@Autowired
	private ProfissionalService profissionalService;
	
	@Override
	public ResponseEntity<Void> manterProfissional(@Valid ProfissionalResource profissionalResource) {
		try {
			
			profissionalService.manterProfissional(profissionalResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			
			log.error("Erro ao cadastrar profissional de estabelecimento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<ProfissionalResource> obterProfissionalById(Integer idProfissional) {
		
		try {
			
			log.info("Requisicao de dados do profissional: "+ idProfissional);
			
			return this.profissionalService.buscarProfissionalById(idProfissional)
						.map(profissional -> ResponseEntity.ok().body(profissional))
						.orElse(ResponseEntity.notFound().build());
			
		} catch (ProfissionalException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			log.info("Erro ao buscar dados do profissional: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<ProfissionalResource>> obterProfissionalByEstabelecimento(Integer idEstabelecimento) {
		
		try {
			
			final List<ProfissionalResource> profissionalRetorno = profissionalService.buscarServicoByEstabelecimento(idEstabelecimento);
			
			return new ResponseEntity<List<ProfissionalResource>>(profissionalRetorno, HttpStatus.OK);
			
		} catch (ProfissionalException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			log.info("Erro ao buscar dados do profissional: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}