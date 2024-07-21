package com.ananda.forohub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface TopicoRepository extends JpaRepository<Topico, Long> {

   boolean existsByTituloAndMensaje(String titulo, String mensaje);

   boolean existsByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);

   void deleteByAutorId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Topico t WHERE t.curso.id = :id")
    void deleteByCursoId(Long id);

}
