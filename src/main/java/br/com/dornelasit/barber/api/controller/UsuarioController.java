/*
 * © 2020 Dit Labs - Copyright - Todos os direitos reservados.
 */
package br.com.dornelasit.barber.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.dornelasit.barber.api.exception.AuthenticationException;
import br.com.dornelasit.barber.api.exception.UsuarioException;
import br.com.dornelasit.barber.api.model.LoginResource;
import br.com.dornelasit.barber.api.model.UsuarioResource;
import br.com.dornelasit.barber.api.services.UsuarioService;
import br.com.dornelasitapi.barber.controller.AppUsuarioApi;
import lombok.extern.log4j.Log4j2;

@CrossOrigin("*")
@RestController
@Log4j2
public class UsuarioController implements AppUsuarioApi{
	
	@Autowired
	private UsuarioService usuarioService;
	@Override
	public ResponseEntity<UsuarioResource> efetuarLogin(@Valid LoginResource loginResource) {
	
	log.error("Requisitando login: " + loginResource.toString());	
		try {
			return this.usuarioService.efetuarLogin(loginResource)
					.map(usuario -> ResponseEntity.ok().body(usuario))
					.orElse(ResponseEntity.notFound().build());
		} catch(AuthenticationException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		}catch (Exception e) {
			log.info("Erro ao buscar usuario: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<Void> manterUsuario(@Valid UsuarioResource usuarioResource) {
		try {
			usuarioService.cadastrarUsuario(usuarioResource);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			log.error("Erro ao cadastrar usuario: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<List<UsuarioResource>> obterListaUsuarios() {
		
		try {
			final List<UsuarioResource> usuarioRetorno = usuarioService.buscarListaUsuarios();
			
			return new ResponseEntity<List<UsuarioResource>>(usuarioRetorno, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Erro ao retornar lista de usuarios: ", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public ResponseEntity<UsuarioResource> obterUsuarioById(Integer idUsuario) {
		
		try {
			log.info("Requisicao de usuario: " + idUsuario);
			
			return this.usuarioService.obterUsuarioById(idUsuario)
					.map(usuario -> ResponseEntity.ok().body(usuario))
					.orElse(ResponseEntity.notFound().build());
			
		} catch(UsuarioException e) {
			log.info(e.getErrorMessage());
			return ResponseEntity.status(e.getErrorCode()).build();
		}catch (Exception e) {
			log.info("Erro ao buscar usuario: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}