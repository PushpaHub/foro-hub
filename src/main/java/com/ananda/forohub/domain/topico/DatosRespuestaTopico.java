package com.ananda.forohub.domain.topico;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        String fecha,
        String estatus,
        Long autorId,
        Long cursoId)

{
    public DatosRespuestaTopico(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha().toString(),
                topico.getEstatus().toString(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        );
}

}

