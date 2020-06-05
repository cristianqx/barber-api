package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.ServicoEntity;

public interface ServicoRepository extends JpaRepository<ServicoEntity, Long>{

	@Query(value = "SELECT * FROM servico WHERE id_estabelecimento = :idEstabelecimento",  nativeQuery = true)
	List<ServicoEntity> findByEstabelecimento(Integer idEstabelecimento);
}
