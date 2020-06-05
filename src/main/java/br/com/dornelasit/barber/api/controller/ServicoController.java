package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.ServicoException;
import br.com.dornelasit.barber.api.model.ServiceResource;
import br.com.dornelasit.barber.api.services.ServicoService;
import br.com.dornelasitapi.barber.controller.AppServicoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class ServicoController implements AppServicoApi{
	
	@Autowired
	private ServicoService servicoService;
	
	@Override
	public ResponseEntity<Void> manterServico(@Valid ServiceResource serviceResource) {
		try {
			servicoService.manterServico(serviceResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			
			log.error("Erro ao cadastrar servico de estabelecimento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<ServiceResource>> obterServicosByEstabelecimento(Integer idEstabelecimento) {
		
		try {
			
			final List<ServiceResource> servicoRetorno = servicoService.buscarServicoByEstabelecimento(idEstabelecimento);
			
			return new ResponseEntity<List<ServiceResource>>(servicoRetorno, HttpStatus.OK);
			
		} catch (ServicoException e) {
			
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
			
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de servicos: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<ServiceResource> obterServicoById(Integer idServico) {

		try {
			
			log.info("Requisicao de dados do servico: " + idServico);
			
			return this.servicoService.buscarServicoById(idServico)
					.map(servico -> ResponseEntity.ok().body(servico))
					.orElse(ResponseEntity.notFound().build());
			
		} catch (ServicoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
			
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de servicos: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
