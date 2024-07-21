package com.ananda.forohub.domain.respuesta;

import com.ananda.forohub.domain.curso.Curso;
import com.ananda.forohub.domain.curso.CursoRepository;
import com.ananda.forohub.domain.topico.*;
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
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ValidacionDePermiso validacionDePermiso;


    // Registrar una nueva respuesta, permitido a todos los perfiles
    public DatosRespuestaRespuesta registrarRespuesta (DatosRegistroRespuesta datosRegistroRespuesta){

        if (!topicoRepository.existsById(datosRegistroRespuesta.topico())) {
            throw new ValidationDeDatosIngresados("El tópico con este id no fue encontrado");
        }
        Topico topico = topicoRepository.getReferenceById(datosRegistroRespuesta.topico());

        // Obtener el id del usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Long usuarioActual = usuarioAutenticado.getId();
        var autor = usuarioRepository.getReferenceById(usuarioActual);

        Respuesta respuesta = new Respuesta(datosRegistroRespuesta, topico, autor);
        respuestaRepository.save(respuesta);

        return new DatosRespuestaRespuesta(respuesta);
    }

    // Listar respuestas, permitido a todos los perfiles
    public Page<DatosListadoRespuesta> listarRespuestas (Pageable paginacion) {

        return respuestaRepository.findAll(paginacion).map(DatosListadoRespuesta::new);
    }

    // Listar solo una respuesta con la id proporcionada, permitido a todos los perfiles
    public DatosRespuestaRespuesta listarUnaRespuesta (Long id) {

        if (!respuestaRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("La respuesta con este id no fue encontrada");
        }

        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        return new DatosRespuestaRespuesta(respuesta);
    }


    // Actualizar respuesta, se puede: mensaje, solucion
    // Permitido al administrador, al moderador y al usuario solo si es el autor de la respuesta
    public DatosRespuestaRespuesta actualizarRespuesta(
            DatosActualizarRespuesta datosActualizarRespuesta){

        if (!respuestaRepository.existsById(datosActualizarRespuesta.id())) {
            throw new ValidationDeDatosIngresados ("La respuesta con este id no fue encontrada");
        }

        Respuesta respuesta = respuestaRepository.getReferenceById(datosActualizarRespuesta.id());

        // Permiso si el usuario es administrador, moderador o el autor
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR", "ROLE_MODERADOR"));
        Long idPermiso = respuesta.getAutor().getId();
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        respuesta.actualizarDatos(datosActualizarRespuesta);
        return new DatosRespuestaRespuesta(respuesta);
    }


    // Eliminar una respuesta
    // Permitido al administrador, al moderador y al usuario solo si es el autor de la respuesta
    public void eliminarRespuesta (Long id) {

        if (!respuestaRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("La respuesta con este id no fue encontrada");
        }

        Respuesta respuesta = respuestaRepository.getReferenceById(id);

        // Permiso si el usuario es administrador, moderador o el autor
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR", "ROLE_MODERADOR"));
        Long idPermiso = respuesta.getAutor().getId();
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        respuestaRepository.deleteById(id);
    }

}
