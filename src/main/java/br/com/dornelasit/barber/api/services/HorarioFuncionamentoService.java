package br.com.dornelasit.barber.api.services;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.HorarioFuncionamentoEntity;
import br.com.dornelasit.barber.api.entity.ProfissionalEntity;
import br.com.dornelasit.barber.api.entity.UsuarioEntity;
import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.exception.ProfissionalException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.HorarioFuncionamentoResource;
import br.com.dornelasit.barber.api.model.ProfissionalResource;
import br.com.dornelasit.barber.api.repositories.HorarioFuncionamentoRepository;
import br.com.dornelasit.barber.util.DataUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HorarioFuncionamentoService {

	@Autowired 
	private HorarioFuncionamentoRepository horarioFuncionamentoRepository;
	
	public void manterHorarioFuncionamento(HorarioFuncionamentoResource horarioFuncionamentoResource) throws Exception  {
		
		HorarioFuncionamentoEntity horarioFuncionamentoEntity = new HorarioFuncionamentoEntity();
		
		try {

			EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
			estabelecimento.setId_estabelecimento(horarioFuncionamentoResource.getEstabelecimento().getId());
			estabelecimento.setDescricao(horarioFuncionamentoResource.getEstabelecimento().getDescricao());
			horarioFuncionamentoEntity.setEstabelecimento(estabelecimento);
			
			horarioFuncionamentoEntity.setDia(DataUtil.converterStringParaCalendarHora(horarioFuncionamentoResource.getDia()));
			
			horarioFuncionamentoEntity.setHorario(DataUtil.converterStringParaTime(horarioFuncionamentoResource.getHorario()));
			horarioFuncionamentoEntity.setFlagDisponivel(horarioFuncionamentoResource.isFlagDisponivel());
			
			ProfissionalEntity profissional = new ProfissionalEntity();
			profissional.setId_profissional(horarioFuncionamentoResource.getProfissional().getId());
			horarioFuncionamentoEntity.setProfissional(profissional);
			
			if(horarioFuncionamentoResource.getId() != null) {
				//desabilita o horario de funcionamento
				horarioFuncionamentoEntity.setId_horario_funcionamento(horarioFuncionamentoResource.getId());
				horarioFuncionamentoEntity.setFlagDisponivel(horarioFuncionamentoResource.isFlagDisponivel());
			}
			
			horarioFuncionamentoRepository.saveAndFlush(horarioFuncionamentoEntity);

			
		} catch (Exception e) {
			log.error("Erro ao manter horario de funcionamento: ", e);
			throw new Exception();
		}	
		
	}
	
	public List<HorarioFuncionamentoResource> buscarListaHorarioFuncionamento() throws Exception {
		
		final List<HorarioFuncionamentoResource> horariosRetorno = new ArrayList<HorarioFuncionamentoResource>();
		
		final List<HorarioFuncionamentoEntity> horarioEntity = horarioFuncionamentoRepository.findAll();
		
		try {
			if(horarioEntity != null) {
				for(int i = 0; i < horarioEntity.size(); i++) {
									
					HorarioFuncionamentoResource horarioEstabelecimentoResource = new HorarioFuncionamentoResource();
					if(horarioEntity.get(i).getFlagDisponivel() == true && horarioEntity.get(i).getProfissional().getFlagAtivo() == true) {
						horarioEstabelecimentoResource.setId(horarioEntity.get(i).getId_horario_funcionamento());
						
						horarioEstabelecimentoResource.setDia(DataUtil.converterDateParaString(horarioEntity.get(i).getDia()));
						horarioEstabelecimentoResource.setHorario(DataUtil.converterTimeString(horarioEntity.get(i).getHorario()));
						horarioEstabelecimentoResource.setFlagDisponivel(horarioEntity.get(i).getFlagDisponivel());
						
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(horarioEntity.get(i).getEstabelecimento().getDescricao());
						horarioEstabelecimentoResource.setEstabelecimento(estabelecimento);
						
						ProfissionalResource profissionalResource = new ProfissionalResource();
						
						profissionalResource.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						profissionalResource.setApelido(horarioEntity.get(i).getProfissional().getApelido());
						profissionalResource.setCelular(horarioEntity.get(i).getProfissional().getCelular());
						profissionalResource.setFlagAtivo(horarioEntity.get(i).getProfissional().getFlagAtivo());
						profissionalResource.setNome(horarioEntity.get(i).getProfissional().getNome());
						horarioEstabelecimentoResource.setProfissional(profissionalResource);
						
						horariosRetorno.add(horarioEstabelecimentoResource);
					}
				}
			}
		} catch (Exception e) {
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);
			throw new Exception();

		}

		
		return horariosRetorno;
	}
	
	public List<HorarioFuncionamentoResource> buscarHorarioFuncionamentoByEstabelecimento(@NonNull Integer idEstabelecimento) throws EstabelecimentoException {
		
		final List<HorarioFuncionamentoResource> horariosRetorno = new ArrayList<HorarioFuncionamentoResource>();
		
		final List<HorarioFuncionamentoEntity> horarioEntity = this.horarioFuncionamentoRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			
			if(horarioEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Estabelecimento não encontrado");
			}
			
			if(horarioEntity != null) {
				for(int i = 0; i < horarioEntity.size(); i++) {
					HorarioFuncionamentoResource horarioEstabelecimentoResource = new HorarioFuncionamentoResource();
					
					if(horarioEntity.get(i).getFlagDisponivel() == true && horarioEntity.get(i).getProfissional().getFlagAtivo() == true) {
						horarioEstabelecimentoResource.setId(horarioEntity.get(i).getId_horario_funcionamento());
						
						horarioEstabelecimentoResource.setDia(DataUtil.converterDateParaString(horarioEntity.get(i).getDia()));
						horarioEstabelecimentoResource.setHorario(DataUtil.converterTimeString(horarioEntity.get(i).getHorario()));
						horarioEstabelecimentoResource.setFlagDisponivel(horarioEntity.get(i).getFlagDisponivel());
						
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(horarioEntity.get(i).getEstabelecimento().getDescricao());
						horarioEstabelecimentoResource.setEstabelecimento(estabelecimento);
						
						ProfissionalResource profissionalResource = new ProfissionalResource();
					
						profissionalResource.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						profissionalResource.setApelido(horarioEntity.get(i).getProfissional().getApelido());
						profissionalResource.setCelular(horarioEntity.get(i).getProfissional().getCelular());
						profissionalResource.setFlagAtivo(horarioEntity.get(i).getProfissional().getFlagAtivo());
						profissionalResource.setNome(horarioEntity.get(i).getProfissional().getNome());
						horarioEstabelecimentoResource.setProfissional(profissionalResource);
						
						horariosRetorno.add(horarioEstabelecimentoResource);
					}
				}
			}
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

		}

		return horariosRetorno;
	}
	
	public List<HorarioFuncionamentoResource> buscarHorarioFuncionamentoByEstabelecimentoAndProfissional(@NonNull Integer idEstabelecimento, @NonNull Integer idProfissional) throws EstabelecimentoException {
		
		final List<HorarioFuncionamentoResource> horariosRetorno = new ArrayList<HorarioFuncionamentoResource>();
		
		final List<HorarioFuncionamentoEntity> horarioEntity = this.horarioFuncionamentoRepository.findByEstabelecimentoANDProfissional(idEstabelecimento, idProfissional);
		
		try {
			
			
			if(horarioEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Dados não encontrados.");
			}
			
			if(horarioEntity != null) {
				for(int i = 0; i < horarioEntity.size(); i++) {
					HorarioFuncionamentoResource horarioEstabelecimentoResource = new HorarioFuncionamentoResource();
					
					if(horarioEntity.get(i).getFlagDisponivel() == true && horarioEntity.get(i).getProfissional().getFlagAtivo() == true) {
						horarioEstabelecimentoResource.setId(horarioEntity.get(i).getId_horario_funcionamento());
						
						horarioEstabelecimentoResource.setDia(DataUtil.converterDateParaString(horarioEntity.get(i).getDia()));
						horarioEstabelecimentoResource.setHorario(DataUtil.converterTimeString(horarioEntity.get(i).getHorario()));
						horarioEstabelecimentoResource.setFlagDisponivel(horarioEntity.get(i).getFlagDisponivel());
						
						EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
						estabelecimento.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						estabelecimento.setDescricao(horarioEntity.get(i).getEstabelecimento().getDescricao());
						horarioEstabelecimentoResource.setEstabelecimento(estabelecimento);
						
						ProfissionalResource profissionalResource = new ProfissionalResource();
					
						profissionalResource.setId(horarioEntity.get(i).getEstabelecimento().getId_estabelecimento());
						profissionalResource.setApelido(horarioEntity.get(i).getProfissional().getApelido());
						profissionalResource.setCelular(horarioEntity.get(i).getProfissional().getCelular());
						profissionalResource.setFlagAtivo(horarioEntity.get(i).getProfissional().getFlagAtivo());
						profissionalResource.setNome(horarioEntity.get(i).getProfissional().getNome());
						horarioEstabelecimentoResource.setProfissional(profissionalResource);
						
						horariosRetorno.add(horarioEstabelecimentoResource);
					}
				}
			}
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao retornar lista de horario de funcionamento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

		}

		return horariosRetorno;
	}
	
}
