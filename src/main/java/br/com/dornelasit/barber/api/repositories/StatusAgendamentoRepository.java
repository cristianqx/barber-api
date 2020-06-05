package br.com.dornelasit.barber.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dornelasit.barber.api.entity.StatusAgendamentoEntity;

public interface StatusAgendamentoRepository extends JpaRepository<StatusAgendamentoEntity, Long> {

}
