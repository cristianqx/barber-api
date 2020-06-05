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
import br.com.dornelasit.barber.api.entity.FormaPagamentoEntity;
import br.com.dornelasit.barber.api.entity.HorarioFuncionamentoEntity;
import br.com.dornelasit.barber.api.entity.ProfissionalEntity;
import br.com.dornelasit.barber.api.entity.ServicoEntity;
import br.com.dornelasit.barber.api.entity.StatusAgendamentoEntity;
import br.com.dornelasit.barber.api.entity.UsuarioEntity;
import br.com.dornelasit.barber.api.exception.AgendaException;
import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.AgendaResource;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.FormasPagamentoResource;
import br.com.dornelasit.barber.api.model.ProfissionalResource;
import br.com.dornelasit.barber.api.model.ServiceResource;
import br.com.dornelasit.barber.api.model.StatusAgendamentoResource;
import br.com.dornelasit.barber.api.model.UsuarioResource;
import br.com.dornelasit.barber.api.repositories.AgendaRepository;
import br.com.dornelasit.barber.api.repositories.FluxoCaixaEntradaRepository;
import br.com.dornelasit.barber.api.repositories.HorarioFuncionamentoRepository;
import br.com.dornelasit.barber.util.DataUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AgendaService {

	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private FluxoCaixaEntradaRepository fluxoCaixaEntradaRepository;
	
	@Autowired
	private HorarioFuncionamentoRepository horarioFuncionamentoRepository;
	
	public void gravarAgenda(AgendaResource agendaResource) throws Exception {
		
		AgendaEntity agendaEntity = new AgendaEntity();
		FluxoCaixaEntradaEntity fluxoCaixaEntrada = new FluxoCaixaEntradaEntity();
		HorarioFuncionamentoEntity horario = new HorarioFuncionamentoEntity();
	
		agendaEntity.setObservacao(agendaResource.getObservacao());
		agendaEntity.setDataAgendada(DataUtil.converterStringParaCalendar(agendaResource.getDataAgendada()));
		agendaEntity.setHoraAgendada(Time.valueOf(agendaResource.getHoraAgendada()));
		EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
		estabelecimento.setId_estabelecimento(agendaResource.getEstabelecimento().getId());
		agendaEntity.setEstabelecimento(estabelecimento);
		
		UsuarioEntity usuario = new UsuarioEntity();
		usuario.setId(agendaResource.getUsuario().getId());
		agendaEntity.setUsuario(usuario);
		
		ServicoEntity servico = new ServicoEntity();
		servico.setId_servico(agendaResource.getServico().getId());
		servico.setValor(Double.valueOf(agendaResource.getServico().getValor().toString()));
		agendaEntity.setServico(servico);
		
		StatusAgendamentoEntity statusAgendamento = new StatusAgendamentoEntity();
		statusAgendamento.setId_status(agendaResource.getStatusAgendamento().getId());
		agendaEntity.setStatus_agendamento(statusAgendamento);
		
		FormaPagamentoEntity formaPagamento = new FormaPagamentoEntity();
		formaPagamento.setId_forma_pagamento(agendaResource.getFormaPagamento().getId());
		agendaEntity.setForma_pagamento(formaPagamento);
		
		ProfissionalEntity profissional = new ProfissionalEntity();
		profissional.setId_profissional(agendaResource.getProfissional().getId());
		agendaEntity.setProfissional(profissional);
		
		
		if(agendaResource.getId() != null && agendaEntity.getStatus_agendamento().getId_status() == 1) {
			// finaliza o agendamento
			agendaEntity.setId_agenda(agendaResource.getId());
			agendaEntity.setHoraFinalizada(Time.valueOf(agendaResource.getHoraFinalizada()));			
			fluxoCaixaEntrada.setAgenda(agendaEntity);
			fluxoCaixaEntrada.setDataEntrada(agendaEntity.getDataAgendada());
			fluxoCaixaEntrada.setHorario(agendaEntity.getHoraFinalizada());
			fluxoCaixaEntrada.setEstabelecimento(agendaEntity.getEstabelecimento());
			fluxoCaixaEntrada.setValor(agendaEntity.getServico().getValor());
		}
		agendaRepository.saveAndFlush(agendaEntity);
		
		//desabilita o horario de agendamento 
		horario.setId_horario_funcionamento(agendaResource.getHorarioFuncionamento().getId());
		horario.setFlagDisponivel(agendaResource.getHorarioFuncionamento().isFlagDisponivel());
		horario.setEstabelecimento(estabelecimento);
		horario.setProfissional(profissional);
		horario.setDia(DataUtil.converterStringParaCalendarHora(agendaResource.getHorarioFuncionamento().getDia()));
		
		horarioFuncionamentoRepository.saveAndFlush(horario);
		
		
		if(agendaResource.getId() != null) {
			fluxoCaixaEntradaRepository.saveAndFlush(fluxoCaixaEntrada);
		}

	}
	
	
	public List<AgendaResource> obterAgendaPorFiltro(String dataAgendamento,Integer idEstabelecimento,
											Integer idUsuario, Integer idProfissional, Integer statusAgendamento) throws AgendaException {
		
		List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		
		try {
						
			if( idEstabelecimento != null && dataAgendamento == null && idUsuario == null && 
					statusAgendamento == null && idProfissional == null) {
				// faz a busca no banco pelo id do estabelecimento
				
				agendaRetorno = this.buscarAgendamentoByEstabelecimento(idEstabelecimento);
				
			} else if(idUsuario != null && dataAgendamento == null && statusAgendamento == null &&
					idEstabelecimento == null && idProfissional == null) {
				// faz a buscva no banco pelo id do usuario
				
				agendaRetorno = this.buscarAgendamentoByUsuario(idUsuario);
				
			} else if(idProfissional != null && idUsuario == null && statusAgendamento == null &&
					  dataAgendamento == null && idEstabelecimento == null) {
				// faz a busca no banco pelo id do profissional
				
				agendaRetorno = this.buscarAgendamentoByProfissional(idProfissional);

			} else if(statusAgendamento != null && idProfissional == null && idUsuario == null && 
					  dataAgendamento == null && idEstabelecimento == null) {
				// faz a busca no banco pelo status do agendamento
				
				agendaRetorno = this.buscarAgendamentoByStatus(statusAgendamento);
				
			} else if( dataAgendamento != null && statusAgendamento == null && idProfissional == null && 
					  idUsuario == null &&  idEstabelecimento == null) {
				// faz a busca no banco pela data
				
				agendaRetorno = this.buscarAgendamentoByData(dataAgendamento);
				
			} else if(dataAgendamento != null && idEstabelecimento != null && statusAgendamento == null && 
					 idProfissional == null && idUsuario == null ) {
				// faz a busca no banco pelo estabelecimento e a data do servico
				
				agendaRetorno = this.buscarAgendamentoByEstabelecimentoEData(idEstabelecimento, dataAgendamento);
				
			} else if(dataAgendamento != null && idProfissional != null && statusAgendamento == null && 
					idEstabelecimento == null && idUsuario == null ) {
				// faz a busca no banco pelo profissional e a data do servico
				
				agendaRetorno = this.buscarAgendamentoByProfissionalEData(idProfissional, dataAgendamento);
				
			} else if(idEstabelecimento != null && idProfissional != null && dataAgendamento == null &&
					  statusAgendamento == null && idUsuario == null ) {
				// faz a busca no banco pelo profissional e a data do servico
				
				agendaRetorno = this.buscarAgendamentoByProfissionalEData(idProfissional, dataAgendamento);
				
			} 
			
			return agendaRetorno;
			
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: : ", e);
			throw new AgendaException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	public List<AgendaResource> buscarAgendamentoByEstabelecimento(@NonNull Integer idEstabelecimento)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Estabelecimento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
						AgendaResource agendaResource = new AgendaResource();
						agendaResource.setId(agendaEntity.get(i).getId_agenda());
						agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
						agendaResource.setDataAgendada(DataUtil.converterDateParaString(agendaEntity.get(i).getDataAgendada()));
						agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
						if(agendaEntity.get(i).getHoraFinalizada() != null) {
							agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
						}
						
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
						agendaResource.setEstabelecimento(estabelecimento);
						
						UsuarioResource usuario = new UsuarioResource();
						usuario.setId(agendaEntity.get(i).getUsuario().getId());
						usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
						usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
						usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
						usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
						usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
						agendaResource.setUsuario(usuario);
						
						ServiceResource servico = new ServiceResource();
						servico.setId(agendaEntity.get(i).getServico().getId_servico());
						servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
						servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
						servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
						agendaResource.setServico(servico);
						
						StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
						statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
						statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
						agendaResource.setStatusAgendamento(statusAgendamento);
						
						FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
						formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
						formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
						agendaResource.setFormaPagamento(formaPagamento);
						
						ProfissionalResource profissional = new ProfissionalResource();
						profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
						profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
						profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
						profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
						EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
						estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
						estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
						profissional.setEstabelecimento(estabelecimentoProfissional);
						agendaResource.setProfissional(profissional);
						
						agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByUsuario(@NonNull Integer idUsuario)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByUsuario(idUsuario);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					agendaResource.setDataAgendada(DataUtil.converterDateParaString(agendaEntity.get(i).getDataAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: : ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByStatus(@NonNull Integer idStatus)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByStatus(idStatus);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					agendaResource.setDataAgendada(DataUtil.converterDateParaString(agendaEntity.get(i).getDataAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: : ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByProfissional(@NonNull Integer idProfissional)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByProfissional(idProfissional);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					agendaResource.setDataAgendada(DataUtil.converterDateParaString(agendaEntity.get(i).getDataAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}

	public List<AgendaResource> buscarAgendamentoByEstabelecimentoEData(@NonNull Integer idEstabelecimento, String dataAgendamento)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByEstabelecimentoEData(idEstabelecimento, dataAgendamento);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setDataAgendada(DataUtil.converterDataTimeParaString(agendaEntity.get(i).getDataAgendada()));
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento:  ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByProfissionalEEstabelecimento(@NonNull Integer idProfissional, Integer idEstabelecimento)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByProfissionalEEstabelecimento(idProfissional, idEstabelecimento);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setDataAgendada(DataUtil.converterDataTimeParaString(agendaEntity.get(i).getDataAgendada()));
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento:  ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByData(String dataAgendamento)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByData(dataAgendamento);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setDataAgendada(DataUtil.converterDataTimeParaString(agendaEntity.get(i).getDataAgendada()));
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento:  ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
	
	public List<AgendaResource> buscarAgendamentoByProfissionalEData(@NonNull Integer idProfissional, String dataAgendamento)
			throws EstabelecimentoException {
		
		final List<AgendaResource> agendaRetorno = new ArrayList<AgendaResource>();
		final List<AgendaEntity> agendaEntity = this.agendaRepository.findByProfissionalEData(idProfissional, dataAgendamento);
		
		try {
			
			if(agendaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Agendamento não encontrado");
			} else {
				for(int i = 0; i < agendaEntity.size(); i++) {
					AgendaResource agendaResource = new AgendaResource();
					agendaResource.setId(agendaEntity.get(i).getId_agenda());
					agendaResource.setObservacao(agendaEntity.get(i).getObservacao());
					agendaResource.setDataAgendada(DataUtil.converterDataTimeParaString(agendaEntity.get(i).getDataAgendada()));
					agendaResource.setHoraAgendada(String.valueOf(agendaEntity.get(i).getHoraAgendada()));
					if(agendaEntity.get(i).getHoraFinalizada() != null) {
						agendaResource.setHoraFinalizada(String.valueOf(agendaEntity.get(i).getHoraFinalizada()));
					}
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(agendaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					agendaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(agendaEntity.get(i).getUsuario().getId());
					usuario.setEmail(agendaEntity.get(i).getUsuario().getEmail());
					usuario.setCelular(agendaEntity.get(i).getUsuario().getCelular());
					usuario.setNome(agendaEntity.get(i).getUsuario().getNome());
					usuario.setSenha(agendaEntity.get(i).getUsuario().getSenha());
					usuario.setApelido(agendaEntity.get(i).getUsuario().getApelido());
					agendaResource.setUsuario(usuario);
					
					ServiceResource servico = new ServiceResource();
					servico.setId(agendaEntity.get(i).getServico().getId_servico());
					servico.setDescricao(agendaEntity.get(i).getServico().getDescricao());
					servico.setDuracao(agendaEntity.get(i).getServico().getDuracao());
					servico.setValor(BigDecimal.valueOf(agendaEntity.get(i).getServico().getValor()));
					agendaResource.setServico(servico);
					
					StatusAgendamentoResource statusAgendamento = new StatusAgendamentoResource();
					statusAgendamento.setId(agendaEntity.get(i).getStatus_agendamento().getId_status());
					statusAgendamento.setDescricao(agendaEntity.get(i).getStatus_agendamento().getDescricao());
					agendaResource.setStatusAgendamento(statusAgendamento);
					
					FormasPagamentoResource formaPagamento = new FormasPagamentoResource();
					formaPagamento.setId(agendaEntity.get(i).getForma_pagamento().getId_forma_pagamento());
					formaPagamento.setDescricao(agendaEntity.get(i).getForma_pagamento().getDescricao());
					agendaResource.setFormaPagamento(formaPagamento);
					
					ProfissionalResource profissional = new ProfissionalResource();
					profissional.setId(agendaEntity.get(i).getProfissional().getId_profissional());
					profissional.setNome(agendaEntity.get(i).getProfissional().getNome());
					profissional.setApelido(agendaEntity.get(i).getProfissional().getApelido());
					profissional.setFlagAtivo(agendaEntity.get(i).getProfissional().getFlagAtivo());
					EstabelecimentoResource estabelecimentoProfissional = new EstabelecimentoResource();
					estabelecimentoProfissional.setId(agendaEntity.get(i).getProfissional().getEstabelecimento().getId_estabelecimento());
					estabelecimentoProfissional.setDescricao(agendaEntity.get(i).getEstabelecimento().getDescricao());
					profissional.setEstabelecimento(estabelecimentoProfissional);
					agendaResource.setProfissional(profissional);
					
					agendaRetorno.add(agendaResource);
				}
			}
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar dados do agendamento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
		
		return agendaRetorno;
	}
}