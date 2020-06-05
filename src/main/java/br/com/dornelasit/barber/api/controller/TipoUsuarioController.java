package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.model.TipoUsuarioResource;
import br.com.dornelasit.barber.api.services.TipoUsuarioService;
import br.com.dornelasitapi.barber.controller.AppTipoUsuarioApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class TipoUsuarioController implements AppTipoUsuarioApi{
	
	@Autowired
	private TipoUsuarioService tipoUsuarioService;
	
	@Override
	public ResponseEntity<Void> manterTipoUsuario(@Valid TipoUsuarioResource tipoUsuarioResource) {
	try {
		tipoUsuarioService.cadastrarTipoUsuario(tipoUsuarioResource);
		
		return ResponseEntity.ok().build();
		
	} catch (Exception e) {
		log.error("Erro ao cadastrar tipo de usuario: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	}

	@Override
	public ResponseEntity<List<TipoUsuarioResource>> obterTipoUsuario() {
		
		try {
			final List<TipoUsuarioResource> tipoUsuarioRetorno = tipoUsuarioService.buscarTipoUsuarios();
			
			return new ResponseEntity<List<TipoUsuarioResource>>(tipoUsuarioRetorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro ao retornar tipo de usuarios: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<TipoUsuarioResource> obterTipoUsuarioById(Integer idTipoUsuario) {
		
		try {
			log.info("Requisicao tipo de usuário: " + idTipoUsuario);
			
			return this.tipoUsuarioService.obterTipoUsuarioById(idTipoUsuario)
					.map(tipoUsuario -> ResponseEntity.ok().body(tipoUsuario))
					.orElse(ResponseEntity.notFound().build());
			
		} catch (Exception e) {
			log.info("Erro ao buscar tipo de usuario: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	
}
