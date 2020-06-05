package br.com.dornelasit.barber.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.repositories.EstabelecimentoRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EstabelecimentoService {

	@Autowired 
	private EstabelecimentoRepository estabelecimentoRepository;
	
	public void cadastrarEstabelecimento(EstabelecimentoResource estabelecimentoResource) throws Exception {
		
		if(estabelecimentoResource.getId() == null) {
			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setDescricao(estabelecimentoResource.getDescricao());
			estabelecimentoRepository.saveAndFlush(estabelecimento);
		} else {
			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setId_estabelecimento(estabelecimentoResource.getId());
			estabelecimento.setDescricao(estabelecimentoResource.getDescricao());
			estabelecimentoRepository.saveAndFlush(estabelecimento);
		}
	}
	
	public List<EstabelecimentoResource> buscarListaEstabelecimentos() {
		
		final List<EstabelecimentoResource> estabelecimentoRetorno = new ArrayList<EstabelecimentoResource>();
		
		final List<EstabelecimentoEntity> estabelecimento = estabelecimentoRepository.findAll();
		
		if(estabelecimento != null) {
			for(int i = 0; i < estabelecimento.size(); i++) {
				EstabelecimentoResource estabelecimentoResource = new EstabelecimentoResource();
				estabelecimentoResource.setDescricao(estabelecimento.get(i).getDescricao());
				estabelecimentoResource.setId(estabelecimento.get(i).getId_estabelecimento());
				
				estabelecimentoRetorno.add(estabelecimentoResource);
			}
		}
		
		return estabelecimentoRetorno;
	}
	
	public Optional<EstabelecimentoResource> obterEstabelecimentoById(@NonNull Integer idUsuario) throws EstabelecimentoException {
		
		EstabelecimentoResource retorno = new EstabelecimentoResource();
		
		final Optional<EstabelecimentoEntity> estabelecimentoEntity = this.estabelecimentoRepository.findById(Long.valueOf(idUsuario));
		
		try {
			if(!estabelecimentoEntity.isPresent()) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Estabelecimento não encontrado");
			} else if (estabelecimentoEntity.isPresent()) {
				retorno.setId(Long.valueOf(idUsuario));
				retorno.setDescricao(estabelecimentoEntity.get().getDescricao());
			}
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao buscar estabelecimento na base de dados: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		
		return Optional.of(retorno);
	}
}
