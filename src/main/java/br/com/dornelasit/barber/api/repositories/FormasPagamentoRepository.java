package br.com.dornelasit.barber.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dornelasit.barber.api.entity.FormaPagamentoEntity;

public interface FormasPagamentoRepository extends JpaRepository<FormaPagamentoEntity, Long>{

}
