package com.ananda.forohub.domain.respuesta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    void deleteByTopicoId(Long id);

    void deleteByAutorId(Long id);

}
