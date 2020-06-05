package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.FluxoCaixaSaidaEntity;

public interface FluxoCaixaSaidaRepository extends JpaRepository<FluxoCaixaSaidaEntity, Long>{

	@Query(value = "SELECT * FROM fluxo_caixa_saida WHERE id_estabelecimento = :idEstabelecimento",  nativeQuery = true)
	List<FluxoCaixaSaidaEntity> findByEstabelecimento(Integer idEstabelecimento);
}
