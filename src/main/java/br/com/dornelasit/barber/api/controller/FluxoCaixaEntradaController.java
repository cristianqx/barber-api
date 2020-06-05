package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.FluxoCaixaEntradaResource;
import br.com.dornelasit.barber.api.services.FluxoCaixaEntradaService;
import br.com.dornelasitapi.barber.controller.AppFluxoCaixaEntradaApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class FluxoCaixaEntradaController implements AppFluxoCaixaEntradaApi{
	
	@Autowired
	private FluxoCaixaEntradaService fluxoCaixaEntradaService;
	
	@Override
	public ResponseEntity<Void> manterFluxoCaixaEntrada(@Valid FluxoCaixaEntradaResource fluxoCaixaEntradaResource) {
		try {
			
			fluxoCaixaEntradaService.gravarValorEntradaCaixa(fluxoCaixaEntradaResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			
			log.error("Erro ao persistir valor no fluxo de entrada: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<FluxoCaixaEntradaResource>> obterFluxoCaixaEntrada() {
		
		try {
			
			final List<FluxoCaixaEntradaResource> retorno = fluxoCaixaEntradaService.buscarListaCaixaEntrada();
			
			return new ResponseEntity<List<FluxoCaixaEntradaResource>>(retorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro retornar valores de fluxo de entrada: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<FluxoCaixaEntradaResource>> obterFluxoCaixaEntradaByEstabelecimento(
			Integer idEstabelecimento) {
		
		try {
			final List<FluxoCaixaEntradaResource> fluxoCaixaEntradaRetorno = 
					fluxoCaixaEntradaService.buscarFluxoCaixaByEstabelecimento(idEstabelecimento);
			
			return new ResponseEntity<List<FluxoCaixaEntradaResource>>(fluxoCaixaEntradaRetorno, HttpStatus.OK);
			
		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			
			log.error("Erro ao retornar fluxo de caixa do estabelecimento " + idEstabelecimento + " ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
