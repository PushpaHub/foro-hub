package com.ananda.forohub.domain.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Perfil findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
