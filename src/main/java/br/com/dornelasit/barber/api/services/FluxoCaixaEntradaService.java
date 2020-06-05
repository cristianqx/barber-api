package br.com.dornelasit.barber.api.services;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.AgendaEntity;
import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.FluxoCaixaEntradaEntity;
import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.AgendaResource;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.FluxoCaixaEntradaResource;
import br.com.dornelasit.barber.api.repositories.FluxoCaixaEntradaRepository;
import br.com.dornelasit.barber.util.DataUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FluxoCaixaEntradaService {

	@Autowired
	private FluxoCaixaEntradaRepository fluxoCaixaEntradaRepository;
	
	public void gravarValorEntradaCaixa(FluxoCaixaEntradaResource fluxoCaixaEntradaResource) throws Exception {
		
		FluxoCaixaEntradaEntity fluxoEntrada = new FluxoCaixaEntradaEntity();
		
		if(fluxoCaixaEntradaResource.getId() == null) {
			fluxoEntrada.setDataEntrada(DataUtil.converterStringParaCalendar(fluxoCaixaEntradaResource.getData()));
			fluxoEntrada.setHorario(DataUtil.converterStringParaTime(fluxoCaixaEntradaResource.getHorario()));
			fluxoEntrada.setValor(Double.parseDouble(fluxoCaixaEntradaResource.getValor().toString()));
			
			AgendaEntity agenda = new AgendaEntity();
			agenda.setId_agenda(fluxoCaixaEntradaResource.getAgenda().getId());
			fluxoEntrada.setAgenda(agenda);
			
			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setDescricao(fluxoCaixaEntradaResource.getEstabelecimento().getDescricao());
			estabelecimento.setId_estabelecimento(fluxoCaixaEntradaResource.getEstabelecimento().getId());
			fluxoEntrada.setEstabelecimento(estabelecimento);
		} else {
			fluxoEntrada.setId_fluxo_caixa_entrada(fluxoCaixaEntradaResource.getId());
			fluxoEntrada.setDataEntrada(DataUtil.converterStringParaCalendar(fluxoCaixaEntradaResource.getData()));
			fluxoEntrada.setHorario(Time.valueOf(fluxoCaixaEntradaResource.getHorario()));
			fluxoEntrada.setValor(Double.parseDouble(fluxoCaixaEntradaResource.getValor().toString()));
			
			AgendaEntity agenda = new AgendaEntity();
			agenda.setId_agenda(fluxoCaixaEntradaResource.getAgenda().getId());
			fluxoEntrada.setAgenda(agenda);
			
			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setDescricao(fluxoCaixaEntradaResource.getEstabelecimento().getDescricao());
			estabelecimento.setId_estabelecimento(fluxoCaixaEntradaResource.getEstabelecimento().getId());
			fluxoEntrada.setEstabelecimento(estabelecimento);
		}
		
		fluxoCaixaEntradaRepository.saveAndFlush(fluxoEntrada);
	}
	
	public List<FluxoCaixaEntradaResource> buscarListaCaixaEntrada() {
		
		final List<FluxoCaixaEntradaResource> fluxoRetorno = new ArrayList<FluxoCaixaEntradaResource>();
		final List<FluxoCaixaEntradaEntity> fluxoCaixaEntity = fluxoCaixaEntradaRepository.findAll();
		
		try {
			if(fluxoCaixaEntity != null) {
				for(int i = 0; i < fluxoCaixaEntity.size(); i++) {
					FluxoCaixaEntradaResource fluxoCaixaRetorno = new FluxoCaixaEntradaResource();
					fluxoCaixaRetorno.setId(fluxoCaixaEntity.get(i).getId_fluxo_caixa_entrada());
					fluxoCaixaRetorno.setData(DataUtil.converterDateParaString(fluxoCaixaEntity.get(i).getDataEntrada()));
					fluxoCaixaRetorno.setHorario(DataUtil.converterTimeString(fluxoCaixaEntity.get(i).getHorario()));
					fluxoCaixaRetorno.setValor(BigDecimal.valueOf(fluxoCaixaEntity.get(i).getValor()));
					
					AgendaResource agenda = new AgendaResource();
					agenda.setId(fluxoCaixaEntity.get(i).getId_fluxo_caixa_entrada());
					fluxoCaixaRetorno.setAgenda(agenda);
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(fluxoCaixaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(fluxoCaixaEntity.get(i).getEstabelecimento().getDescricao());
					fluxoCaixaRetorno.setEstabelecimento(estabelecimento);
					
					fluxoRetorno.add(fluxoCaixaRetorno);
				}
			}
		} catch (Exception e) {
			log.error("Erro ao buscar estabelecimento na base de dados: ", e);
		}

		
		return fluxoRetorno;
		
	}
	
	public List<FluxoCaixaEntradaResource> buscarFluxoCaixaByEstabelecimento(@NonNull Integer idEstabelecimento) throws EstabelecimentoException {
		
		final List<FluxoCaixaEntradaResource> fluxoCaixaRetorno = new ArrayList<FluxoCaixaEntradaResource>();
		final List<FluxoCaixaEntradaEntity> fluxoCaixaEntity = this.fluxoCaixaEntradaRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			if(fluxoCaixaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Estabelecimento não encontrado");
			} else {
				for(int i = 0; i < fluxoCaixaEntity.size(); i++) {
					FluxoCaixaEntradaResource fluxoCaixaEntradaResource = new FluxoCaixaEntradaResource();
					fluxoCaixaEntradaResource.setHorario(DataUtil.converterTimeString(fluxoCaixaEntity.get(i).getHorario()));
					fluxoCaixaEntradaResource.setData(DataUtil.converterDateParaString(fluxoCaixaEntity.get(i).getDataEntrada()));
					fluxoCaixaEntradaResource.setId(fluxoCaixaEntity.get(i).getId_fluxo_caixa_entrada());
					fluxoCaixaEntradaResource.setValor(BigDecimal.valueOf(fluxoCaixaEntity.get(i).getValor()));
					
					AgendaResource agenda = new AgendaResource();
					agenda.setId(fluxoCaixaEntity.get(i).getId_fluxo_caixa_entrada());
					fluxoCaixaEntradaResource.setAgenda(agenda);
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(fluxoCaixaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(fluxoCaixaEntity.get(i).getEstabelecimento().getDescricao());
					fluxoCaixaEntradaResource.setEstabelecimento(estabelecimento);
					
					fluxoCaixaRetorno.add(fluxoCaixaEntradaResource);
				}
			}
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar fluxo de caixa do estabalecimento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
			
		return fluxoCaixaRetorno;
	}
}
