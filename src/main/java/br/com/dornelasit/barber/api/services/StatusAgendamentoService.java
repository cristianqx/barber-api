package br.com.dornelasit.barber.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.StatusAgendamentoEntity;
import br.com.dornelasit.barber.api.exception.StatusAgendamentoException;
import br.com.dornelasit.barber.api.model.StatusAgendamentoResource;
import br.com.dornelasit.barber.api.repositories.StatusAgendamentoRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StatusAgendamentoService {

	@Autowired
	private StatusAgendamentoRepository statusAgendamentoRepository;
	public List<StatusAgendamentoResource> buscarStatusAgendamento() throws Exception {
		
		final List<StatusAgendamentoResource> retorno = new ArrayList<StatusAgendamentoResource>();
		final List<StatusAgendamentoEntity> status = statusAgendamentoRepository.findAll();
		
		if(status != null) {
			
			for(int i = 0; i < status.size(); i++) {
				StatusAgendamentoResource statusResource = new StatusAgendamentoResource();
				statusResource.setDescricao(status.get(i).getDescricao());
				statusResource.setId(status.get(i).getId_status());
				retorno.add(statusResource);
			}
		}
		
		return retorno;
	}
	
	public Optional<StatusAgendamentoResource> obterStatusAgendamentoById(@NonNull Integer idStatus) throws StatusAgendamentoException {
		
		StatusAgendamentoResource retornoStatusAgendamento = new StatusAgendamentoResource();
		
		final Optional<StatusAgendamentoEntity> statusEntity = this.statusAgendamentoRepository.findById(Long.valueOf(idStatus));
		
		try {
			if(!statusEntity.isPresent()) {
				throw new StatusAgendamentoException(HttpStatus.UNAUTHORIZED.value(), "Status de agendamento não encontrado");
			} else if(statusEntity.isPresent()) {
				retornoStatusAgendamento.setId(Long.valueOf(idStatus));
				retornoStatusAgendamento.setDescricao(statusEntity.get().getDescricao());
			}

		} catch (StatusAgendamentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao buscar status de agendamento: ", e);
			throw new StatusAgendamentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		
		return Optional.of(retornoStatusAgendamento);
	}
}
