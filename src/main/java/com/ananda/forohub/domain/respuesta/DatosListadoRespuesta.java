package com.ananda.forohub.domain.respuesta;


public record DatosListadoRespuesta(Long id, String mensaje, Long topico,
                                    String fecha, Long autor, String solucion) {

    public DatosListadoRespuesta(Respuesta respuesta){
        this(
            respuesta.getId(),
            respuesta.getMensaje(),
            respuesta.getTopico().getId(),
            respuesta.getFecha().toString(),
            respuesta.getAutor().getId(),
            respuesta.getSolucion().toString());
        }

}
