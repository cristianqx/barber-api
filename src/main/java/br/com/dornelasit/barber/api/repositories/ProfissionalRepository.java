package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.ProfissionalEntity;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long>{

	@Query(value="SELECT * FROM profissional WHERE id_estabelecimento = :idEstabelecimento", nativeQuery = true)
	List<ProfissionalEntity> findByEstabelecimento(Integer idEstabelecimento);
}
