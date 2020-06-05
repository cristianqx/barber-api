package br.com.dornelasit.barber.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dornelasit.barber.api.entity.FluxoCaixaEntradaEntity;

public interface FluxoCaixaEntradaRepository extends JpaRepository<FluxoCaixaEntradaEntity, Long>{

	@Query(value = "SELECT * FROM fluxo_caixa_entrada WHERE id_estabelecimento = :idEstabelecimento",  nativeQuery = true)
	List<FluxoCaixaEntradaEntity> findByEstabelecimento(Integer idEstabelecimento);
}
