package br.com.dornelasit.barber.api.services;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.EstabelecimentoEntity;
import br.com.dornelasit.barber.api.entity.FluxoCaixaSaidaEntity;
import br.com.dornelasit.barber.api.entity.UsuarioEntity;
import br.com.dornelasit.barber.api.exception.EstabelecimentoException;
import br.com.dornelasit.barber.api.model.EstabelecimentoResource;
import br.com.dornelasit.barber.api.model.FluxoCaixaSaidaResource;
import br.com.dornelasit.barber.api.model.TipoUsuarioResource;
import br.com.dornelasit.barber.api.model.UsuarioResource;
import br.com.dornelasit.barber.api.repositories.FluxoCaixaSaidaRepository;
import br.com.dornelasit.barber.util.DataUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FluxoCaixaSaidaService {

	@Autowired
	private FluxoCaixaSaidaRepository fluxoCaixaSaidaRepository;
	
	public void gravarValorSaidaCaixa(FluxoCaixaSaidaResource fluxoCaixaSaidaResource) throws EstabelecimentoException {

		FluxoCaixaSaidaEntity fluxoSaida = new FluxoCaixaSaidaEntity();
		
		try {
			
			if(!fluxoCaixaSaidaResource.getUsuario().getTipoUsuario().getIdTipoUsuario().equals(2)) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Permissão negada. Este usuário não possui permissão para "
						+ "realizar essa operação!");
			}
			
			if(fluxoCaixaSaidaResource.getId() == null) {
				fluxoSaida.setDataSaida(DataUtil.converterStringParaCalendar(fluxoCaixaSaidaResource.getData()));
				fluxoSaida.setHorario(DataUtil.converterStringParaTime(fluxoCaixaSaidaResource.getHorario()));
				fluxoSaida.setValor(Double.parseDouble(fluxoCaixaSaidaResource.getValor().toString()));
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setDescricao(fluxoCaixaSaidaResource.getEstabelecimento().getDescricao());
				estabelecimento.setId_estabelecimento(fluxoCaixaSaidaResource.getEstabelecimento().getId());
				fluxoSaida.setEstabelecimento(estabelecimento);
				
				UsuarioEntity usuario = new UsuarioEntity();
				usuario.setId(fluxoCaixaSaidaResource.getUsuario().getId());
				fluxoSaida.setUsuario(usuario);
			} else {
				fluxoSaida.setId_fluxo_saida(fluxoCaixaSaidaResource.getId());
				fluxoSaida.setDataSaida(DataUtil.converterStringParaCalendar(fluxoCaixaSaidaResource.getData()));
				fluxoSaida.setValor(Double.parseDouble(fluxoCaixaSaidaResource.getValor().toString()));
				
				EstabelecimentoEntity estabelecimento = new EstabelecimentoEntity();
				estabelecimento.setDescricao(fluxoCaixaSaidaResource.getEstabelecimento().getDescricao());
				estabelecimento.setId_estabelecimento(fluxoCaixaSaidaResource.getEstabelecimento().getId());
				fluxoSaida.setEstabelecimento(estabelecimento);
				
				UsuarioEntity usuario = new UsuarioEntity();
				usuario.setId(fluxoCaixaSaidaResource.getUsuario().getId());
				fluxoSaida.setUsuario(usuario);
			}
			
			
			fluxoCaixaSaidaRepository.saveAndFlush(fluxoSaida);
		
			
		} catch (EstabelecimentoException e) {
			throw e;
		} catch (Exception e) {
			log.error("Erro ao persistir fluxo de caixa do estabalecimento: ", e);
			throw new EstabelecimentoException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	public List<FluxoCaixaSaidaResource> buscarListaCaixaSaida() {
		
		final List<FluxoCaixaSaidaResource> fluxoRetorno = new ArrayList<FluxoCaixaSaidaResource>();
		final List<FluxoCaixaSaidaEntity> fluxoCaixaEntity = fluxoCaixaSaidaRepository.findAll();
		
		try {
			if(fluxoCaixaEntity != null) {
				for(int i = 0; i < fluxoCaixaEntity.size(); i++) {
					FluxoCaixaSaidaResource fluxoCaixaRetorno = new FluxoCaixaSaidaResource();
					fluxoCaixaRetorno.setId(fluxoCaixaEntity.get(i).getId_fluxo_saida());
					fluxoCaixaRetorno.setHorario(DataUtil.converterTimeString(fluxoCaixaEntity.get(i).getHorario()));
					fluxoCaixaRetorno.setData(DataUtil.converterDateParaString(fluxoCaixaEntity.get(i).getDataSaida()));
					fluxoCaixaRetorno.setValor(BigDecimal.valueOf(fluxoCaixaEntity.get(i).getValor()));
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(fluxoCaixaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(fluxoCaixaEntity.get(i).getEstabelecimento().getDescricao());
					fluxoCaixaRetorno.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(fluxoCaixaEntity.get(i).getUsuario().getId());
					usuario.setApelido(fluxoCaixaEntity.get(i).getUsuario().getApelido());
					usuario.setCelular(fluxoCaixaEntity.get(i).getUsuario().getCelular());
					usuario.setEmail(fluxoCaixaEntity.get(i).getUsuario().getEmail());
					usuario.setNome(fluxoCaixaEntity.get(i).getUsuario().getNome());
					
					TipoUsuarioResource tipoUsuario = new TipoUsuarioResource();
					tipoUsuario.setIdTipoUsuario(Integer.valueOf(fluxoCaixaEntity.get(i).getUsuario().
																	getTipo_usuario().getId_tipo_usuario().toString()));
					tipoUsuario.setDescricao(fluxoCaixaEntity.get(i).getUsuario().getTipo_usuario().getDescricao());
					usuario.setTipoUsuario(tipoUsuario);
					
					fluxoCaixaRetorno.setUsuario(usuario);
					
					fluxoRetorno.add(fluxoCaixaRetorno);
				}
			}
		} catch (Exception e) {
			log.error("Erro ao buscar estabelecimento na base de dados: ", e);
		}

		
		return fluxoRetorno;
		
	}
	
	public List<FluxoCaixaSaidaResource> buscarFluxoCaixaByEstabelecimento(@NonNull Integer idEstabelecimento) throws EstabelecimentoException {
		
		final List<FluxoCaixaSaidaResource> fluxoCaixaRetorno = new ArrayList<FluxoCaixaSaidaResource>();
		final List<FluxoCaixaSaidaEntity> fluxoCaixaEntity = this.fluxoCaixaSaidaRepository.findByEstabelecimento(idEstabelecimento);
		
		try {
			if(fluxoCaixaEntity.size() == 0) {
				throw new EstabelecimentoException(HttpStatus.UNAUTHORIZED.value(), "Estabelecimento não encontrado");
			} else {
				for(int i = 0; i < fluxoCaixaEntity.size(); i++) {
					FluxoCaixaSaidaResource fluxoCaixaSaidaResource = new FluxoCaixaSaidaResource();
					fluxoCaixaSaidaResource.setHorario(DataUtil.converterTimeString(fluxoCaixaEntity.get(i).getHorario()));
					fluxoCaixaSaidaResource.setData(DataUtil.converterDateParaString(fluxoCaixaEntity.get(i).getDataSaida()));
					fluxoCaixaSaidaResource.setId(fluxoCaixaEntity.get(i).getId_fluxo_saida());
					fluxoCaixaSaidaResource.setValor(BigDecimal.valueOf(fluxoCaixaEntity.get(i).getValor()));
					
					EstabelecimentoResource estabelecimento = new EstabelecimentoResource();
					estabelecimento.setId(fluxoCaixaEntity.get(i).getEstabelecimento().getId_estabelecimento());
					estabelecimento.setDescricao(fluxoCaixaEntity.get(i).getEstabelecimento().getDescricao());
					fluxoCaixaSaidaResource.setEstabelecimento(estabelecimento);
					
					UsuarioResource usuario = new UsuarioResource();
					usuario.setId(fluxoCaixaEntity.get(i).getUsuario().getId());
					usuario.setApelido(fluxoCaixaEntity.get(i).getUsuario().getApelido());
					usuario.setCelular(fluxoCaixaEntity.get(i).getUsuario().getCelular());
					usuario.setEmail(fluxoCaixaEntity.get(i).getUsuario().getEmail());
					usuario.setNome(fluxoCaixaEntity.get(i).getUsuario().getNome());
					
					TipoUsuarioResource tipoUsuario = new TipoUsuarioResource();
					tipoUsuario.setIdTipoUsuario(Integer.valueOf(fluxoCaixaEntity.get(i).getUsuario().
																	getTipo_usuario().getId_tipo_usuario().toString()));
					tipoUsuario.setDescricao(fluxoCaixaEntity.get(i).getUsuario().getTipo_usuario().getDescricao());
					usuario.setTipoUsuario(tipoUsuario);
					
					fluxoCaixaSaidaResource.setUsuario(usuario);
					
					fluxoCaixaRetorno.add(fluxoCaixaSaidaResource);
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