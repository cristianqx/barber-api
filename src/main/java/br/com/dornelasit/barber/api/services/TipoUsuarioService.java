package br.com.dornelasit.barber.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.Optional;



import br.com.dornelasit.barber.api.entity.TipoUsuarioEntity;
import br.com.dornelasit.barber.api.model.TipoUsuarioResource;
import br.com.dornelasit.barber.api.repositories.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {

	@Autowired
	private TipoUsuarioRepository tipoUsuarioRepository;
	
	public void cadastrarTipoUsuario(TipoUsuarioResource tipoUsuarioResource)throws Exception {
		
		TipoUsuarioEntity tipoUsuario = new TipoUsuarioEntity();
		tipoUsuario.setDescricao(tipoUsuarioResource.getDescricao());
		tipoUsuarioRepository.saveAndFlush(tipoUsuario);
	}
	
	public List<TipoUsuarioResource> buscarTipoUsuarios() {
		
		final List<TipoUsuarioResource> tipoUsuario = new ArrayList<TipoUsuarioResource>();
		
		final List<TipoUsuarioEntity> tipoUsuarioRetorno = tipoUsuarioRepository.findAll();
 		
		
		if(tipoUsuarioRetorno != null) {
			for(int i = 0; i < tipoUsuarioRetorno.size(); i++) {
				TipoUsuarioResource objeto = new TipoUsuarioResource();
				objeto.setDescricao(tipoUsuarioRetorno.get(i).getDescricao());
				objeto.setIdTipoUsuario(Integer.valueOf(tipoUsuarioRetorno.get(i).getId_tipo_usuario().toString()));
				tipoUsuario.add(objeto);
			}
		}
		
		return tipoUsuario;
	}
	
	public Optional<TipoUsuarioResource> obterTipoUsuarioById(@NonNull Integer idTipoUsuario) throws Exception {
		
		TipoUsuarioResource retorno = new TipoUsuarioResource();
		
		final Optional<TipoUsuarioEntity> tipoUsuarioEntity = this.tipoUsuarioRepository.findById(Long.valueOf(idTipoUsuario));
		
		if(tipoUsuarioEntity.isPresent()) {
			retorno.setIdTipoUsuario(Integer.valueOf(tipoUsuarioEntity.get().getId_tipo_usuario().toString()));
			retorno.setDescricao(tipoUsuarioEntity.get().getDescricao());
		}
		
		return Optional.of(retorno);
	}
}
