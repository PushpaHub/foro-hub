package com.ananda.forohub.domain.topico;

import com.ananda.forohub.domain.curso.Curso;
import com.ananda.forohub.domain.curso.CursoRepository;
import com.ananda.forohub.domain.perfil.Perfil;
import com.ananda.forohub.domain.usuario.DatosRespuestaUsuario;
import com.ananda.forohub.domain.usuario.Usuario;
import com.ananda.forohub.domain.usuario.UsuarioRepository;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import com.ananda.forohub.infra.errores.ValidationDeRoles;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.w3c.dom.ls.LSOutput;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActualizarTopicoService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public DatosRespuestaTopico actualizar (DatosActualizarTopico datosActualizarTopico,
                                            Authentication authentication){

        if (!topicoRepository.existsById(datosActualizarTopico.id())) {
            throw new ValidationDeDatosIngresados ("El topico con este id no fue encontrado");
        }

        Topico topico = topicoRepository.getReferenceById(datosActualizarTopico.id());
        if (!usuarioPuedeActualizar(authentication, topico)){
            throw  new ValidationDeRoles("No tienes permiso para cambiar este tópico");
        }

        validarDatosDeActualizar(datosActualizarTopico, topico);

        Curso curso = new Curso();
        if (datosActualizarTopico.curso() != null) {
            curso = cursoRepository.getReferenceById(datosActualizarTopico.curso());
        } else {
            curso = null;
        }
        topico.actualizarDatos(datosActualizarTopico, curso);

        return new DatosRespuestaTopico(topico);
    }

    private boolean usuarioPuedeActualizar(Authentication authentication, Topico topico) {

        Set<String> roles = getRoles(authentication);

        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Long idUsuario = usuarioAutenticado.getId();

        // Permitir la actualización si el usuario es moderador, administrador o el propietario del tópico
        return roles.contains("ROLE_MODERADOR") || roles.contains("ROLE_ADMINISTRADOR")
                || topico.getAutor().getId().equals(idUsuario);
    }

    private Set<String> getRoles(Authentication authentication) {

        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Long idUsuario = usuarioAutenticado.getId();

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        Set<Perfil> perfiles = usuario.getPerfiles();

        perfiles.forEach(perfil -> System.out.println("Perfil: " + perfil.getNombre()));

        return perfiles.stream()
                .map(perfil -> "ROLE_" + perfil.getNombre())
                .collect(Collectors.toSet());
    }

    private void validarDatosDeActualizar(DatosActualizarTopico datosActualizarTopico, Topico topico) {
        // Validar si el nuevo título o mensaje ya existen en otro tópicoo
        String nuevoTitulo = topico.getTitulo();
        String nuevoMensaje = topico.getMensaje();
        if (datosActualizarTopico.titulo() != null) nuevoTitulo = datosActualizarTopico.titulo();
        if (datosActualizarTopico.mensaje() != null) nuevoMensaje = datosActualizarTopico.mensaje();
        if (topicoRepository.existsByTituloAndMensajeAndIdNot(
                nuevoTitulo, nuevoMensaje, datosActualizarTopico.id())) {
            throw new ValidationDeDatosIngresados("El topico con el mismo título y el mismo mensaje ya existe");
        }
    }
    
}
