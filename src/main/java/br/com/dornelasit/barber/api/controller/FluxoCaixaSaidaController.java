package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.FluxoCaixaSaidaResource;
import br.com.dornelasit.barber.api.services.FluxoCaixaSaidaService;
import br.com.dornelasitapi.barber.controller.AppFluxoCaixaSaidaApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class FluxoCaixaSaidaController implements AppFluxoCaixaSaidaApi{
	
	@Autowired
	private FluxoCaixaSaidaService fluxoCaixaSaidaService;
	
	@Override
	public ResponseEntity<Void> manterFluxoCaixaSaida(@Valid FluxoCaixaSaidaResource fluxoCaixaSaida) {
		
		try {
			
			fluxoCaixaSaidaService.gravarValorSaidaCaixa(fluxoCaixaSaida);
			
			return ResponseEntity.ok().build();
			
		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			
			log.error("Erro ao manter informações na base dados: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<FluxoCaixaSaidaResource>> obterFluxoCaixaSaida() {
		try {
			
			final List<FluxoCaixaSaidaResource> retorno = fluxoCaixaSaidaService.buscarListaCaixaSaida();
			
			return new ResponseEntity<List<FluxoCaixaSaidaResource>>(retorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro retornar valores de fluxo de Saida: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<FluxoCaixaSaidaResource>> obterFluxoCaixaSaidaByEstabelecimento(
			Integer idEstabelecimento) {
		try {
			final List<FluxoCaixaSaidaResource> fluxoCaixaSaidaRetorno = 
					fluxoCaixaSaidaService.buscarFluxoCaixaByEstabelecimento(idEstabelecimento);
			
			return new ResponseEntity<List<FluxoCaixaSaidaResource>>(fluxoCaixaSaidaRetorno, HttpStatus.OK);
			
		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			
			log.error("Erro ao retornar fluxo de caixa do estabelecimento " + idEstabelecimento + " ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
