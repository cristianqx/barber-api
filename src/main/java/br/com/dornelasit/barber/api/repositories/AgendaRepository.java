package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.AgendaEntity;

public interface AgendaRepository extends JpaRepository<AgendaEntity, Long>{

	@Query(value="SELECT * FROM agenda WHERE id_estabelecimento = :idEstabelecimento ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByEstabelecimento(Integer idEstabelecimento);
	
	@Query(value="SELECT * FROM agenda WHERE id_usuario = :idUsuario ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByUsuario(Integer idUsuario);
	
	@Query(value="SELECT * FROM agenda WHERE id_profissional = :idProfissional ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByProfissional(Integer idProfissional);
	
	@Query(value="SELECT * FROM agenda WHERE id_status = :idStatus ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByStatus(Integer idStatus);
	
	@Query(value="SELECT * FROM agenda WHERE data_agendada = :dataAgendamento AND id_estabelecimento = :idEstabelecimento ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByEstabelecimentoEData(Integer idEstabelecimento, String dataAgendamento);
	
	@Query(value="SELECT * FROM agenda WHERE id_profissional = :idProfissional AND data_agendada = :dataAgendamento ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByProfissionalEData(Integer idProfissional, String dataAgendamento);
	
	@Query(value="SELECT * FROM agenda WHERE data_agendada = :dataAgendamento ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByData(String dataAgendamento);
	
	@Query(value="SELECT * FROM agenda WHERE id_profissional = :idProfissional AND id_estabelecimento = :idEstabelecimento ORDER BY data_agendada", nativeQuery = true)
	List<AgendaEntity> findByProfissionalEEstabelecimento(Integer idEstabelecimento, Integer idProfissional);
}
