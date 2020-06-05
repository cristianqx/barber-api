package br.com.dornelasit.barber.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.StatusAgendamentoException;
import br.com.dornelasit.barber.api.model.StatusAgendamentoResource;
import br.com.dornelasit.barber.api.services.StatusAgendamentoService;
import br.com.dornelasitapi.barber.controller.AppStatusAgendamentoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class StatusAgendamentoController implements AppStatusAgendamentoApi{

	@Autowired
	private StatusAgendamentoService statusAgendamentoService;
	
	@Override
	public ResponseEntity<List<StatusAgendamentoResource>> obterStatusAgendamento() {
		
		try {
			final List<StatusAgendamentoResource> statusRetorno = statusAgendamentoService.buscarStatusAgendamento();
			
			return new ResponseEntity<List<StatusAgendamentoResource>>(statusRetorno, HttpStatus.OK);
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de status de agendamento: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<StatusAgendamentoResource> obterStatusAgendamentoById(Integer idStatusAgendamento) {
		
			try {
				log.info("Requisicao de status de agendamento: " + idStatusAgendamento);
				
				return this.statusAgendamentoService.obterStatusAgendamentoById(idStatusAgendamento)
						.map(status -> ResponseEntity.ok().body(status))
						.orElse(ResponseEntity.notFound().build());
				
			}catch (StatusAgendamentoException e) {
				log.info(e.getErrorMessage());
				return ResponseEntity.status(e.getErrorCode()).build();
			} catch (Exception e) {
				log.info("Erro ao status de agendamento: ", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	}