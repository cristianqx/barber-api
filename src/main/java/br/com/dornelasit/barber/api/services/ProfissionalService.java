package br.com.dornelasit.barber.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.ProfissionalEntity;
import br.com.dornelasit.barber.api.exception.ProfissionalException;
import br.com.dornelasit.barber.api.exception.ServicoException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.ProfissionalResource;
import br.com.dornelasit.barber.api.repositories.ProfissionalRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProfissionalService {

	@Autowired
	private ProfissionalRepository profissionalRepository;
	
	public void manterProfissional(ProfissionalResource profissionalResource) throws Exception {
		
		ProfissionalEntity profissionalEntity = new ProfissionalEntity();
		
		try {
			if(profissionalResource.getId() == null) {
				profissionalEntity.setNome(profissionalResource.getNome());
				profissionalEntity.setCelular(profissionalResource.getCelular());
				profissionalEntity.setFlagAtivo(profissionalResource.isFlagAtivo());
				profissionalEntity.setApelido(profissionalResource.getApelido());
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setId_estabelecimento(profissionalResource.getEstabelecimento().getId());
				estabelecimento.setDescricao(profissionalResource.getEstabelecimento().getDescricao());
				profissionalEntity.setEstabelecimento(estabelecimento);
			} else {
				profissionalEntity.setId_profissional(profissionalResource.getId());
				profissionalEntity.setNome(profissionalResource.getNome());
				profissionalEntity.setCelular(profissionalResource.getCelular());
				profissionalEntity.setFlagAtivo(profissionalResource.isFlagAtivo());
				profissionalEntity.setApelido(profissionalResource.getApelido());
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setId_estabelecimento(profissionalResource.getEstabelecimento().getId());
				estabelecimento.setDescricao(profissionalResource.getEstabelecimento().getDescricao());
				profissionalEntity.setEstabelecimento(estabelecimento);
			}
			
			
			profissionalRepository.saveAndFlush(profissionalEntity);
			
		} catch (Exception e) {
			throw e;
		}
	}

	public Optional<ProfissionalResource> buscarProfissionalById(@NonNull Integer idProfissional) throws ProfissionalException {
		
		ProfissionalResource retorno = new ProfissionalResource();
		
		final Optional<ProfissionalEntity> profissionalEntity = this.profissionalRepository.findById(Long.valueOf(idProfissional));
		
		try {
			
			if(!profissionalEntity.isPresent()) {
				throw new ProfissionalException(HttpStatus.UNAUTHORIZED.value(), "Profissional não encontrado na base de dados");
			} else if (profissionalEntity.isPresent()) {
				
				retorno.setNome(profissionalEntity.get().getNome());
				retorno.setApelido(profissionalEntity.get().getApelido());
				retorno.setFlagAtivo(profissionalEntity.get().getFlagAtivo());
				retorno.setNome(profissionalEntity.get().getNome());
				retorno.setCelular(profissionalEntity.get().getCelular());
				retorno.setId(profissionalEntity.get().getId_profissional());
				
				EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
				estabelecimento.setId(profissionalEntity.get().getId_profissional());
				estabelecimento.setDescricao(profissionalEntity.get().getEstabelecimento().getDescricao());
				
				retorno.setEstabelecimento(estabelecimento);
			}
		} catch (ProfissionalException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao buscar profissional cadastrado");
		}
		
		return Optional.of(retorno);
		
	}
	
	public List<ProfissionalResource> buscarServicoByEstabelecimento(@NonNull Integer idEstabelecimento) throws ProfissionalException {
		
		final List<ProfissionalResource> profissionalRetorno = new ArrayList<ProfissionalResource>();
		final List<ProfissionalEntity> profissionalEntity = this.profissionalRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			
			if(profissionalEntity.size() == 0) {
				throw new ProfissionalException(HttpStatus.UNAUTHORIZED.value(), "Profissional não encontrado");
			} else {
				for(int i = 0; i < profissionalEntity.size(); i++) {
					if(profissionalEntity.get(i).getFlagAtivo().equals(true)) {
						ProfissionalResource profissionalResource = new ProfissionalResource();
						profissionalResource.setApelido(profissionalEntity.get(i).getApelido());
						profissionalResource.setCelular(profissionalEntity.get(i).getCelular());
						profissionalResource.setId(profissionalEntity.get(i).getId_profissional());
						profissionalResource.setFlagAtivo(profissionalEntity.get(i).getFlagAtivo());
						profissionalResource.setNome(profissionalEntity.get(i).getNome());
						
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(profissionalEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(profissionalEntity.get(i).getEstabelecimento().getDescricao());
						
						profissionalResource.setEstabelecimento(estabelecimento);
						
						profissionalRetorno.add(profissionalResource);
					}
				}
			}
			
			
		} catch (ProfissionalException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar lista de profissionais do estabelecimento: ", e);
			throw new ProfissionalException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return profissionalRetorno;
	}
}
