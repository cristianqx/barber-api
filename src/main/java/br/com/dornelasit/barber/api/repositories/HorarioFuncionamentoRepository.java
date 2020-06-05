package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.HorarioFuncionamentoEntity;

public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamentoEntity, Long>{

	@Query(value = "SELECT * FROM horario_funcionamento WHERE id_estabelecimento = :idEstabelecimento",  nativeQuery = true)
	List<HorarioFuncionamentoEntity> findByEstabelecimento(Integer idEstabelecimento);
	
	@Query(value = "SELECT * FROM horario_funcionamento WHERE id_estabelecimento = :idEstabelecimento AND id_profissional =:idProfissional",  nativeQuery = true)
	List<HorarioFuncionamentoEntity> findByEstabelecimentoANDProfissional(Integer idEstabelecimento, Integer idProfissional);
}
