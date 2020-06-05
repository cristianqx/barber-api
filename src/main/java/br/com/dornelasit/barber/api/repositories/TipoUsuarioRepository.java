package br.com.dornelasit.barber.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dornelasit.barber.api.entity.TipoUsuarioEntity;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {

}
