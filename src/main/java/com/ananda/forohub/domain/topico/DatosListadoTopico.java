package com.ananda.forohub.domain.topico;

public record DatosListadoTopico(Long id, String titulo, String mensaje,
                                 String fecha, String estatus,
                                 Long autor, Long curso) {

    public DatosListadoTopico(Topico topico){
        this(
            topico.getId(),
            topico.getTitulo(),
            topico.getMensaje(),
            topico.getFecha().toString(),
            topico.getEstatus().toString(),
            topico.getAutor().getId(),
            topico.getCurso().getId());
    }

}
