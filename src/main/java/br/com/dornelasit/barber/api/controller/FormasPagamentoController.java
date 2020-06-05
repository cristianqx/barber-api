package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.model.FormasPagamentoResource;
import br.com.dornelasit.barber.api.services.FormasPagamentoService;
import br.com.dornelasitapi.barber.controller.AppFormasPagamentoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class FormasPagamentoController implements AppFormasPagamentoApi{
	
	@Autowired
	private FormasPagamentoService formasPagamentoService;
	
	@Override 
	public ResponseEntity<Void> manterFormasPagamento(@Valid FormasPagamentoResource formasPagamentoResource) {

		try {
			this.formasPagamentoService.manterFormaPagamento(formasPagamentoResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			
			log.error("Erro ao cadastrar forma de pagamento na base de dados: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<FormasPagamentoResource>> obterFormasPagamento() {
		
		try {
			final List<FormasPagamentoResource> retorno = this.formasPagamentoService.listarFormasPagamento();
			
			return new ResponseEntity<List<FormasPagamentoResource>>(retorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro ao retornar formas de pagamento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();		
		}
		
	}

}
