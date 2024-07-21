package com.ananda.forohub.domain.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNombreAndCategoria(String nombre, String categoria);

    boolean existsByNombreAndCategoriaAndIdNot(String nuevoNombre, String nuevaCategoria, Long id);
}
