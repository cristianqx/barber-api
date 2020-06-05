package br.com.dornelasit.barber.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dis.infra.util.Crypt;
import br.com.dornelasit.barber.api.constants.AppConstants;
import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.ProfissionalEntity;
import br.com.dornelasit.barber.api.entity.TipoUsuarioEntity;
import br.com.dornelasit.barber.api.entity.UsuarioEntity;
import br.com.dornelasit.barber.api.exception.AuthenticationException;
import br.com.dornelasit.barber.api.exception.UsuarioException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.LoginResource;
import br.com.dornelasit.barber.api.model.ProfissionalResource;
import br.com.dornelasit.barber.api.model.TipoUsuarioResource;
import br.com.dornelasit.barber.api.model.UsuarioResource;
import br.com.dornelasit.barber.api.repositories.ProfissionalRepository;
import br.com.dornelasit.barber.api.repositories.UsuarioRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProfissionalRepository profissionalRepository;
	
	
	
	
	public Optional<UsuarioResource> efetuarLogin(final LoginResource loginResource)
	throws AuthenticationException{
		
		UsuarioResource retorno = new UsuarioResource();
	    Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(loginResource.getEmail().toLowerCase());
				
		try {

			if(usuarioEntity.isPresent()) {
				
				final String senhaCriptografada = Crypt.criptyMD5(loginResource.getSenha());
				
				usuarioEntity = usuarioEntity.filter(user -> user.getSenha().equals(senhaCriptografada));
				
				if(!usuarioEntity.isPresent()) {
					throw new AuthenticationException(HttpStatus.UNAUTHORIZED.value(), "Senha inválida");
				}
				
				retorno.setId(usuarioEntity.get().getId());
				retorno.setApelido(usuarioEntity.get().getApelido());
				retorno.setCelular(usuarioEntity.get().getCelular());
				retorno.setEmail(usuarioEntity.get().getEmail());
				retorno.setNome(usuarioEntity.get().getNome());
				retorno.setSenha(usuarioEntity.get().getSenha());
				
				EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
				estabelecimento.setDescricao(usuarioEntity.get().getEstabelecimento_preferencia().getDescricao());
				estabelecimento.setId(usuarioEntity.get().getEstabelecimento_preferencia().getId_estabelecimento());
				retorno.setEstabelecimentoPreferencia(estabelecimento);
				
				if(usuarioEntity.get().getTipo_usuario() != null) {
					TipoUsuarioResource tipoUsuario = new TipoUsuarioResource();
					tipoUsuario.setIdTipoUsuario(Integer.valueOf(usuarioEntity.get().getTipo_usuario().getId_tipo_usuario().toString()));
					tipoUsuario.setDescricao(usuarioEntity.get().getTipo_usuario().getDescricao());
					retorno.setTipoUsuario(tipoUsuario);
				}
				
			}
			
		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao autenticar usuário no sistema: ", e);
			throw new AuthenticationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return Optional.of(retorno);
	}
	public void cadastrarUsuario(UsuarioResource usuarioResource) throws Exception {
		UsuarioEntity usuario = new UsuarioEntity();
		
		if(usuarioResource.getId() == null) {	 // se for um usuario novo
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setId_estabelecimento(usuarioResource.getEstabelecimentoPreferencia().getId());
				
				usuario.setEstabelecimento_preferencia(estabelecimento);
				usuario.setNome(usuarioResource.getNome());
				usuario.setEmail(usuarioResource.getEmail());
				usuario.setSenha(Crypt.criptyMD5(usuarioResource.getSenha()));
				usuario.setApelido(usuarioResource.getApelido());
				usuario.setCelular(usuarioResource.getCelular());
				usuario.setFlagAtivo(usuarioResource.isFlagAtivo());
				TipoUsuarioEntity tipoUsuario = new TipoUsuarioEntity();
				tipoUsuario.setId_tipo_usuario(Long.valueOf(usuarioResource.getTipoUsuario().getIdTipoUsuario().toString()));
				usuario.setTipo_usuario(tipoUsuario);
				
				if(usuarioResource.getTipoUsuario().getIdTipoUsuario().equals(AppConstants.TIPO_USUARIO_PROFISSIONAL)) {
					ProfissionalEntity profissionalEntity = new ProfissionalEntity();
					profissionalEntity.setApelido(usuarioResource.getApelido());
					profissionalEntity.setCelular(usuarioResource.getCelular());
					profissionalEntity.setFlagAtivo(usuarioResource.isFlagAtivo());
					profissionalEntity.setNome(usuarioResource.getNome());
					profissionalEntity.setEstabelecimento(estabelecimento);
					this.profissionalRepository.saveAndFlush(profissionalEntity);
				}	

			
		} else {
			// edita informacoes do usuario
			usuario.setId(usuarioResource.getId());
			TipoUsuarioEntity tipoUsuario = new TipoUsuarioEntity();
			tipoUsuario.setId_tipo_usuario(Long.valueOf(usuarioResource.getTipoUsuario().getIdTipoUsuario().toString()));
			
			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setId_estabelecimento(usuarioResource.getEstabelecimentoPreferencia().getId());
			
			usuario.setEstabelecimento_preferencia(estabelecimento);
			usuario.setNome(usuarioResource.getNome());
			usuario.setEmail(usuarioResource.getEmail());
			usuario.setSenha(Crypt.criptyMD5(usuarioResource.getSenha()));
			usuario.setApelido(usuarioResource.getApelido());
			usuario.setCelular(usuarioResource.getCelular());
			usuario.setTipo_usuario(tipoUsuario);
			usuario.setFlagAtivo(usuarioResource.isFlagAtivo());

			if(usuarioResource.getTipoUsuario().getIdTipoUsuario().equals(AppConstants.TIPO_USUARIO_PROFISSIONAL)) {
				ProfissionalEntity profissionalEntity = new ProfissionalEntity();
				profissionalEntity.setApelido(usuarioResource.getApelido());
				profissionalEntity.setCelular(usuarioResource.getCelular());
				profissionalEntity.setFlagAtivo(usuarioResource.isFlagAtivo());
				profissionalEntity.setNome(usuarioResource.getNome());
				profissionalEntity.setEstabelecimento(estabelecimento);
				this.profissionalRepository.saveAndFlush(profissionalEntity);
			}	
		}

		
		
		usuarioRepository.saveAndFlush(usuario);		
	}

	
	public List<UsuarioResource> buscarListaUsuarios() {
		
		final List<UsuarioResource> usuarioRetorno = new ArrayList<UsuarioResource>();
		
		final List<UsuarioEntity> usuario = usuarioRepository.findAll();
		
		if(usuario != null) {
			for(int i = 0; i < usuario.size(); i++) {
				UsuarioResource usuarioResource = new UsuarioResource();
				usuarioResource.setApelido(usuario.get(i).getApelido());
				usuarioResource.setCelular(usuario.get(i).getCelular());
				usuarioResource.setEmail(usuario.get(i).getEmail());
				usuarioResource.setId(usuario.get(i).getId());
				usuarioResource.setNome(usuario.get(i).getNome());
				usuarioResource.setSenha(usuario.get(i).getSenha());
				
				EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
				estabelecimento.setId(usuario.get(i).getEstabelecimento_preferencia().getId_estabelecimento());
				estabelecimento.setDescricao(usuario.get(i).getEstabelecimento_preferencia().getDescricao());
				
				TipoUsuarioResource tipoUsuario = new TipoUsuarioResource();
				if(usuario.get(i).getTipo_usuario() != null) {
					tipoUsuario.setIdTipoUsuario(Integer.valueOf(usuario.get(i).getTipo_usuario().getId_tipo_usuario().toString()));
					tipoUsuario.setDescricao(usuario.get(i).getTipo_usuario().getDescricao());
				}
				
				usuarioResource.setEstabelecimentoPreferencia(estabelecimento);
				usuarioResource.setTipoUsuario(tipoUsuario);
				usuarioRetorno.add(usuarioResource);
			}
		}
		
		return usuarioRetorno;
	}
	
	public Optional<UsuarioResource> obterUsuarioById(@NonNull Integer idUsuario) throws UsuarioException {
		
		UsuarioResource retorno = new UsuarioResource();
		
		final Optional<UsuarioEntity> usuarioEntity = this.usuarioRepository.findById(Long.valueOf(idUsuario));
		
		try {
			
			if(!usuarioEntity.isPresent()) {
				throw new UsuarioException(HttpStatus.UNAUTHORIZED.value(), "Usuario não encontrado");
			} else if(usuarioEntity.isPresent()) {
				retorno.setId(Long.valueOf(idUsuario));
				retorno.setApelido(usuarioEntity.get().getApelido());
				retorno.setCelular(usuarioEntity.get().getCelular());
				retorno.setEmail(usuarioEntity.get().getEmail());
				retorno.setNome(usuarioEntity.get().getNome());
				retorno.setSenha(usuarioEntity.get().getSenha());

				EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
				estabelecimento.setId(usuarioEntity.get().getEstabelecimento_preferencia().getId_estabelecimento());
				estabelecimento.setDescricao(usuarioEntity.get().getEstabelecimento_preferencia().getDescricao());
				retorno.setEstabelecimentoPreferencia(estabelecimento);
				
				TipoUsuarioResource tipoUsuario = new TipoUsuarioResource();
				
				if(usuarioEntity.get().getTipo_usuario() != null) {
					tipoUsuario.setIdTipoUsuario(Integer.valueOf(usuarioEntity.get().getTipo_usuario().getId_tipo_usuario().toString()));
					tipoUsuario.setDescricao(usuarioEntity.get().getTipo_usuario().getDescricao());
					retorno.setTipoUsuario(tipoUsuario);
				}
				
			}
		} catch (UsuarioException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao buscar usuario: ", e);
			throw new UsuarioException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}

		
		return Optional.of(retorno);
		
	}
	
}
