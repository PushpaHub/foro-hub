package com.ananda.forohub.domain.topico;

import com.ananda.forohub.domain.curso.Curso;
import com.ananda.forohub.domain.respuesta.Respuesta;
import com.ananda.forohub.domain.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Table (name = "topicos")
@Entity (name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private Estatus estatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Respuesta> respuestas = new HashSet<>();


    public Topico(DatosRegistroTopico datosRegistroTopico, Usuario autor, Curso curso) {
        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.fecha = LocalDateTime.now();
        this.estatus = Estatus.NO_solucionado;
        this.autor = autor;
        this.curso = curso;
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico, Curso curso) {
        if (datosActualizarTopico.titulo() != null)
            this.titulo = datosActualizarTopico.titulo();

        if (datosActualizarTopico.mensaje() != null)
            this.mensaje = datosActualizarTopico.mensaje();

        if (datosActualizarTopico.estatus() != null)
            this.estatus = Estatus.valueOf(datosActualizarTopico.estatus());

        if (datosActualizarTopico.curso() != null)
            this.curso = curso;

    }

}
