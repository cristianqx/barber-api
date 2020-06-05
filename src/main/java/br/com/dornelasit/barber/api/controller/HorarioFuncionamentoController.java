package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.HorarioFuncionamentoResource;
import br.com.dornelasit.barber.api.services.HorarioFuncionamentoService;
import br.com.dornelasitapi.barber.controller.AppHorarioFuncionamentoApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class HorarioFuncionamentoController implements AppHorarioFuncionamentoApi {
	
	@Autowired
	private HorarioFuncionamentoService horarioFuncionamentoService;
	
	@Override
	public ResponseEntity<Void> manterHorarioFuncionamento(
			@Valid HorarioFuncionamentoResource horarioFuncionamentoResource) {
		
		try {
			horarioFuncionamentoService.manterHorarioFuncionamento(horarioFuncionamentoResource);
			
			return ResponseEntity.ok().build();
			
		} catch(Exception e) {
			log.error("Erro ao cadastrar horario de estabelecimento: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<HorarioFuncionamentoResource>> obterHorarioFuncionamentoByEstabelecimento(
			Integer idEstabelecimento) {
		try {
			final List<HorarioFuncionamentoResource> horarioFuncionamentoRetorno = 
					horarioFuncionamentoService.buscarHorarioFuncionamentoByEstabelecimento(idEstabelecimento);
			
			return new ResponseEntity<List<HorarioFuncionamentoResource>>(horarioFuncionamentoRetorno, HttpStatus.OK);
			
		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<HorarioFuncionamentoResource>> obterListaHorarioEstabelecimento() {
		try {
			final List<HorarioFuncionamentoResource> horarioFuncionamentoRetorno = horarioFuncionamentoService.buscarListaHorarioFuncionamento();
			
			return new ResponseEntity<List<HorarioFuncionamentoResource>>(horarioFuncionamentoRetorno, HttpStatus.OK);
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<HorarioFuncionamentoResource>> obterHorarioFuncionamentoByEstabelecimentoAndProfissional(
			Integer idEstabelecimento, Integer idProfissional) {
		try {
			final List<HorarioFuncionamentoResource> horarioFuncionamentoRetorno = 
					horarioFuncionamentoService.buscarHorarioFuncionamentoByEstabelecimentoAndProfissional(idEstabelecimento, idProfissional);
			
			return new ResponseEntity<List<HorarioFuncionamentoResource>>(horarioFuncionamentoRetorno, HttpStatus.OK);
			
		} catch (EstabelecimentoException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		} catch (Exception e) {
			
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
