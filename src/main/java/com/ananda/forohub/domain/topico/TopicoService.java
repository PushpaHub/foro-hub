package com.ananda.forohub.domain.topico;

import com.ananda.forohub.domain.curso.Curso;
import com.ananda.forohub.domain.curso.CursoRepository;
import com.ananda.forohub.domain.respuesta.RespuestaRepository;
import com.ananda.forohub.domain.usuario.Usuario;
import com.ananda.forohub.domain.usuario.UsuarioRepository;
import com.ananda.forohub.infra.errores.ValidacionDePermiso;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private ValidacionDePermiso validacionDePermiso;


    // Registrar un nuevo tópico, abierto a todos
    public DatosRespuestaTopico registrar (DatosRegistroTopico datosRegistroTopico){

        if (topicoRepository.existsByTituloAndMensaje
                (datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())) {
            throw new ValidationDeDatosIngresados("El topico con el mismo título y el mismo mensaje ya existe");
        }
        if (!cursoRepository.existsById(datosRegistroTopico.curso())) {
            throw new ValidationDeDatosIngresados("El curso con este id no fue encontrado");
        }

        // Obtener el id del usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Long usuarioActual = usuarioAutenticado.getId();

        var autor = usuarioRepository.getReferenceById(usuarioActual);
        var curso = cursoRepository.getReferenceById(datosRegistroTopico.curso());
        Topico topico = new Topico(datosRegistroTopico, autor, curso);
        topicoRepository.save(topico);

        return new DatosRespuestaTopico(topico);
    }

    // Listar tópicos, permitido a todos los perfiles
    public Page<DatosListadoTopico> listarTopicos (Pageable paginacion) {

        return topicoRepository.findAll(paginacion).map(DatosListadoTopico::new);
    }

    // Listar solo un tópico con la id proporcionada, permitido a todos los perfiles
    public DatosRespuestaTopico listarUnTopico (Long id) {

        if (!topicoRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("El topico con este id no fue encontrado");
        }

        Topico topico = topicoRepository.getReferenceById(id);
        return new DatosRespuestaTopico(topico);
    }

    // Actualizar tópico, se puede: titulo, mensaje, estatus, curso
    // Permitido al administrador, al moderador y al usuario solo si es autor del tópico
    public DatosRespuestaTopico actualizarTopico(DatosActualizarTopico datosActualizarTopico){

        if (!topicoRepository.existsById(datosActualizarTopico.id())) {
            throw new ValidationDeDatosIngresados ("El topico con este id no fue encontrado");
        }

        Topico topico = topicoRepository.getReferenceById(datosActualizarTopico.id());

        // Permiso si el usuario es administrador, moderador o el autor
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR", "ROLE_MODERADOR"));
        Long idPermiso = topico.getAutor().getId();
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        // Validar si el nuevo título o mensaje ya existen en otro tópicoo
        String nuevoTitulo = topico.getTitulo();
        String nuevoMensaje = topico.getMensaje();
        if (datosActualizarTopico.titulo() != null) nuevoTitulo = datosActualizarTopico.titulo();
        if (datosActualizarTopico.mensaje() != null) nuevoMensaje = datosActualizarTopico.mensaje();
        if (topicoRepository.existsByTituloAndMensajeAndIdNot(
                nuevoTitulo, nuevoMensaje, datosActualizarTopico.id())) {
            throw new ValidationDeDatosIngresados("El topico con el mismo título y el mismo mensaje ya existe");
        }

        Curso curso = new Curso();
        if (datosActualizarTopico.curso() != null) {
            curso = cursoRepository.getReferenceById(datosActualizarTopico.curso());
        } else {
            curso = null;
        }

        topico.actualizarDatos(datosActualizarTopico, curso);
        return new DatosRespuestaTopico(topico);
    }


    // Eliminar un tópico
    // Permitido al administrador, al moderador y al usuario solo si es autor del tópico
    public void eliminarTopico (Long id) {

        if (!topicoRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("El topico con este id no fue encontrado");
        }

        Topico topico = topicoRepository.getReferenceById(id);

        // Permiso si el usuario es administrador, moderador o el autor
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR", "ROLE_MODERADOR"));
        Long idPermiso = topico.getAutor().getId();
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        // eliminar también todas las respuestas de este topico
        respuestaRepository.deleteByTopicoId(id);
        topicoRepository.deleteById(id);
    }

}
