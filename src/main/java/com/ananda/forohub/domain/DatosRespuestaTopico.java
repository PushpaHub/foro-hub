package com.ananda.forohub.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DatosRespuestaTopico(Long id, String titulo, String mensaje,
                                   String fecha, String estatus, Long autor,
                                   String curso){

    public DatosRespuestaTopico(Topico topico){
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFecha(),
                topico.getEstatus().toString(), topico.getAutor(), topico.getCurso().toString());
    }

}

