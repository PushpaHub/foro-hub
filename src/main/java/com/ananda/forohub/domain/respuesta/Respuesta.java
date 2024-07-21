package com.ananda.forohub.domain.respuesta;

import com.ananda.forohub.domain.topico.Estatus;
import com.ananda.forohub.domain.topico.Topico;
import com.ananda.forohub.domain.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;
    @Enumerated(EnumType.STRING)
    private Estatus solucion;


    public Respuesta(DatosRegistroRespuesta datosRegistroRespuesta, Topico topico, Usuario autor) {
        this.mensaje = datosRegistroRespuesta.mensaje();
        this.topico = topico;
        this.fecha = LocalDateTime.now();
        this.autor = autor;
        if (datosRegistroRespuesta.solucion() != null)
            this.solucion = this.solucion = Estatus.valueOf(datosRegistroRespuesta.solucion());
        else
            this.solucion = Estatus.NO_solucionado;
    }

    public void actualizarDatos(DatosActualizarRespuesta datosActualizarRespuesta) {
        if (datosActualizarRespuesta.mensaje() != null)
            this.mensaje = datosActualizarRespuesta.mensaje();

        if (datosActualizarRespuesta.solucion() != null)
            this.solucion = Estatus.valueOf(datosActualizarRespuesta.solucion());
    }

}
