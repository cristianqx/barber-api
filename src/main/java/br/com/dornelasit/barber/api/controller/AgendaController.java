package br.com.dornelasit.barber.api.controller;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.model.AgendaResource;
import br.com.dornelasit.barber.api.services.AgendaService;
import br.com.dornelasitapi.barber.controller.AppAgendamentoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class AgendaController implements AppAgendamentoApi {

	@Autowired
	private AgendaService agendaService;
	
	@Override
	public ResponseEntity<Void> manterAgendamento(@Valid AgendaResource agendaResource) {
		
		try {
			agendaService.gravarAgenda(agendaResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			log.error("Erro ao persistir agendamento na base de dados: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<AgendaResource>> obterAgendamentos(@Valid Integer idEstabelecimento,
			@Valid String dataAgendamento, @Valid Integer idUsuario, @Valid Integer idProfissional,
			@Valid Integer statusAgendamento) {
		
		try {
							
			final List<AgendaResource> agendaRetorno = agendaService.obterAgendaPorFiltro(dataAgendamento, idEstabelecimento,
																						  idUsuario, idProfissional, statusAgendamento);
			
			return new ResponseEntity<List<AgendaResource>>(agendaRetorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro ao retornar agendamento da data: " + dataAgendamento + " : ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}


