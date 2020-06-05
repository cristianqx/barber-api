package br.com.dornelasit.barber.api.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.ServicoEntity;
import br.com.dornelasit.barber.api.exception.ServicoException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.ServiceResource;
import br.com.dornelasit.barber.api.repositories.ServicoRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ServicoService {

	@Autowired
	private ServicoRepository servicoRepository;
	
	
	public void manterServico(ServiceResource serviceResource) throws Exception {
		
		ServicoEntity servicoEntity = new ServicoEntity();
		
		try {
			if(serviceResource.getId() == null) {
				servicoEntity.setDescricao(serviceResource.getDescricao());
				servicoEntity.setDuracao(serviceResource.getDuracao());
				servicoEntity.setFlagAtivo(serviceResource.isFlagAtivo());
				servicoEntity.setValor(Double.parseDouble(serviceResource.getValor().toString()));
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setId_estabelecimento(serviceResource.getEstabelecimento().getId());
				estabelecimento.setDescricao(serviceResource.getEstabelecimento().getDescricao());
				servicoEntity.setEstabelecimento(estabelecimento);
			} else {
				servicoEntity.setId_servico(serviceResource.getId());
				servicoEntity.setDescricao(serviceResource.getDescricao());
				servicoEntity.setDuracao(serviceResource.getDuracao());
				servicoEntity.setFlagAtivo(serviceResource.isFlagAtivo());
				servicoEntity.setValor(Double.parseDouble(serviceResource.getValor().toString()));
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setId_estabelecimento(serviceResource.getEstabelecimento().getId());
				estabelecimento.setDescricao(serviceResource.getEstabelecimento().getDescricao());
				servicoEntity.setEstabelecimento(estabelecimento);
			}
			
			servicoRepository.saveAndFlush(servicoEntity);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<ServiceResource> buscarServicoByEstabelecimento(@NonNull Integer idEstabelecimento) throws ServicoException {
		
		final List<ServiceResource> servicoRetorno = new ArrayList<ServiceResource>();
		
		final List<ServicoEntity> servicoEntity = this.servicoRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			
			if(servicoEntity.size() == 0) {
				throw new ServicoException(HttpStatus.UNAUTHORIZED.value(), "Servico não encontrado");
			}
			
			if(servicoEntity != null) {
				for(int i = 0; i < servicoEntity.size(); i++) {
					if(servicoEntity.get(i).getFlagAtivo() == true) {
						// se o servico estiver habilitado
						ServiceResource serviceResource = new ServiceResource();
						serviceResource.setDescricao(servicoEntity.get(i).getDescricao());
						serviceResource.setDuracao(servicoEntity.get(i).getDuracao());
						serviceResource.setFlagAtivo(servicoEntity.get(i).getFlagAtivo());
						serviceResource.setValor(BigDecimal.valueOf(servicoEntity.get(i).getValor()));
						serviceResource.setId(servicoEntity.get(i).getId_servico());
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(servicoEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(servicoEntity.get(i).getEstabelecimento().getDescricao());
						serviceResource.setEstabelecimento(estabelecimento);
						
						servicoRetorno.add(serviceResource);
					}
				}
			}
		} catch (ServicoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar lista de servicos do estabelecimento: ", e);
			throw new ServicoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return servicoRetorno;
	}
	
	
	public Optional<ServiceResource> buscarServicoById(@NonNull Integer idServico) throws ServicoException {
		
		final ServiceResource servicoRetorno = new ServiceResource();
		
		final Optional<ServicoEntity> servicoEntity = this.servicoRepository.findById(Long.valueOf(idServico));
		
		try {
			
			if(!servicoEntity.isPresent()) {
				throw new ServicoException(HttpStatus.UNAUTHORIZED.value(), "Servico não encontrado");
			} else {
				
				if(servicoEntity.get().getFlagAtivo() == true) {
					// se o servico estiver habilitado
					servicoRetorno.setDescricao(servicoEntity.get().getDescricao());
					servicoRetorno.setDuracao(servicoEntity.get().getDuracao());
					servicoRetorno.setFlagAtivo(servicoEntity.get().getFlagAtivo());
					servicoRetorno.setValor(BigDecimal.valueOf(servicoEntity.get().getValor()));
					servicoRetorno.setId(servicoEntity.get().getId_servico());
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(servicoEntity.get().getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(servicoEntity.get().getEstabelecimento().getDescricao());
					servicoRetorno.setEstabelecimento(estabelecimento);
				}
			}
		} catch (ServicoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar o servico: ", e);
			throw new ServicoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return Optional.of(servicoRetorno);
	}
}
