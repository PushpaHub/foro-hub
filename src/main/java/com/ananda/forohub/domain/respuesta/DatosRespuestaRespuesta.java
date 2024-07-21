package com.ananda.forohub.domain.respuesta;

import com.ananda.forohub.domain.topico.Topico;

public record DatosRespuestaRespuesta(
        Long id,
        String mensaje,
        Long topico,
        String fecha,
        Long autorId,
        String solucion)

{
    public DatosRespuestaRespuesta(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getTopico().getId(),
                respuesta.getFecha().toString(),
                respuesta.getAutor().getId(),
                respuesta.getSolucion().toString()
        );
}

}

