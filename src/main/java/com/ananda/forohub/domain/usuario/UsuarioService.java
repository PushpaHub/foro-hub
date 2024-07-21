package com.ananda.forohub.domain.usuario;

import com.ananda.forohub.domain.perfil.DatosActualizarPerfilesDelUsuario;
import com.ananda.forohub.domain.perfil.Perfil;
import com.ananda.forohub.domain.perfil.PerfilRepository;
import com.ananda.forohub.domain.respuesta.RespuestaRepository;
import com.ananda.forohub.domain.topico.TopicoRepository;
import com.ananda.forohub.infra.errores.ValidacionDePermiso;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import com.ananda.forohub.infra.errores.ValidationDeRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private ValidacionDePermiso validacionDePermiso;


    // Registrar un nuevo usuario, abierto a todos
    public DatosRespuestaUsuario registrar (DatosRegistroUsuario datosRegistroUsuario){

        if (usuarioRepository.existsByLogin(datosRegistroUsuario.login())) {
            throw new ValidationDeDatosIngresados("El usuario con este email ya existe");
        }

        // Bycript de la clave
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(datosRegistroUsuario.clave());

        // Agrega el perfil "USUARIO" a nuevo usuario
        Perfil perfilUsuario = perfilRepository.findByNombre("USUARIO");
        Set<Perfil> perfiles = new HashSet<>();
        perfiles.add(perfilUsuario);

        Usuario usuario = new Usuario(
                datosRegistroUsuario.nombre(), datosRegistroUsuario.login(), password, perfiles);
        usuarioRepository.save(usuario);

        return new DatosRespuestaUsuario(usuario);
    }

    // Listar usuarios, solo permitido al administrador
    public Page<DatosListadoUsuario> listarUsuarios (Pageable paginacion) {

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        return usuarioRepository.findAll(paginacion)
                .map(DatosListadoUsuario::new);
    }

    // Listar solo un usuario con la id proporcionada
    // Permitido al administrador y al mismo usuario
    public DatosRespuestaUsuario listarUnUsuario (Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("El usuario con este id no fue encontrado");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = id;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Usuario usuario = usuarioRepository.getReferenceById(id);
        return new DatosRespuestaUsuario(usuario);
    }


    // Actualizar un usuario, nombre o clave
    // Permitido al administrador y al mismo usuario
    public DatosRespuestaUsuario actualizarUsuario (DatosActualizarUsuario datosActualizarUsuario) {

        if (!usuarioRepository.existsById(datosActualizarUsuario.id())) {
            throw new ValidationDeDatosIngresados("El usuario con este id no fue encontrado");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = datosActualizarUsuario.id();
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Usuario usuario = usuarioRepository.getReferenceById(datosActualizarUsuario.id());

        // Bycript de la clave
        PasswordEncoder passwordEncoder = null;
        String password;
        if (datosActualizarUsuario.clave() != null) {
            passwordEncoder = new BCryptPasswordEncoder();
            password = passwordEncoder.encode(datosActualizarUsuario.clave());
        } else {
            password = null;
        }

        usuario.actualizarDatos(datosActualizarUsuario.nombre(), password);
        return new DatosRespuestaUsuario(usuario);
    }


    // Eliminar un usuario
    // Permitido al administrador y al mismo usuario
    public void eliminarUsuario (Long id){

        if (!usuarioRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados ("El usuario con este id no fue encontrado");
        }

        // Permiso para el usuario mismo y para el administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = id;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        //Eliminar todos los tópicos de este usuario y todas las respuestas a estos tópicos
        topicoRepository.deleteByAutorId(id);

        // Eliminar todas las respuestas de este usuario
        respuestaRepository.deleteByAutorId(id);

        //Eliminar el usuario
        usuarioRepository.deleteById(id);
    }


    // Añadir un perfil a un usuario, solo permitido al administrador
    public DatosRespuestaUsuario anexarPerfil(DatosActualizarPerfilesDelUsuario datosAnexarPerfil) {

        Usuario usuario = usuarioRepository.findById(datosAnexarPerfil.usuarioId())
                .orElseThrow(() -> new ValidationDeDatosIngresados("Usuario no encontrado"));
        Perfil perfil = perfilRepository.findById(datosAnexarPerfil.perfilId())
                .orElseThrow(() -> new ValidationDeDatosIngresados("Perfil no encontrado"));

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        usuario.getPerfiles().add(perfil);
        usuarioRepository.save(usuario);

        return new DatosRespuestaUsuario(usuario);
    }


    // Eliminar un perfil a un usuario, solo permitido al administrador.
    // El perfil "USUARIO" no se puede eliminar
    public DatosRespuestaUsuario eliminarPerfil(DatosActualizarPerfilesDelUsuario datosEliminarPerfil) {

        Usuario usuario = usuarioRepository.findById(datosEliminarPerfil.usuarioId())
                .orElseThrow(() -> new ValidationDeDatosIngresados("Usuario no encontrado"));
        Perfil perfil = perfilRepository.findById(datosEliminarPerfil.perfilId())
                .orElseThrow(() -> new ValidationDeDatosIngresados("Perfil no encontrado"));
        if (perfilRepository.getReferenceById(
                datosEliminarPerfil.perfilId()).getNombre().equals("USUARIO")){
            throw new ValidationDeDatosIngresados("El perfil USUARIO no se puede eliminar");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        // El usuario debe tener minimo un perfil
        if (usuario.getPerfiles().size() < 2) {
            throw new ValidationDeRoles("El usuario tiene solo un perfil, no se puede eliminar");
        }

        usuario.getPerfiles().removeIf(per -> per.getId().equals(datosEliminarPerfil.perfilId()));
        usuarioRepository.save(usuario);

        return new DatosRespuestaUsuario(usuario);
    }

}
