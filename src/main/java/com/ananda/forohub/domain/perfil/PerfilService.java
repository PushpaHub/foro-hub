package com.ananda.forohub.domain.perfil;

import com.ananda.forohub.domain.topico.TopicoRepository;
import com.ananda.forohub.domain.usuario.*;
import com.ananda.forohub.infra.errores.ValidacionDePermiso;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import com.ananda.forohub.infra.errores.ValidationDeRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private ValidacionDePermiso validacionDePermiso;


    // Registrar un nuevo perfil, solo permitido al administrador
    public DatosRespuestaPerfil registrarPerfil (DatosRegistroPerfil datosRegistroPerfil){

        if (perfilRepository.existsByNombre(datosRegistroPerfil.nombre())) {
            throw new ValidationDeDatosIngresados("El perfil con este nombre ya existe");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Perfil perfil = new Perfil(datosRegistroPerfil);
        perfilRepository.save(perfil);
        return new DatosRespuestaPerfil(perfil);
    }


    // Listar perfiles, solo permitido al administrador
    public Page<DatosListadoPerfil> listarPerfiles (Pageable paginacion) {

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        return perfilRepository.findAll(paginacion)
                .map(DatosListadoPerfil::new);
    }

    // Listar solo un perfil con la id proporcionada
    // Permitido al administrador
    public DatosRespuestaPerfil listarUnPerfil(Long id) {

        if (!perfilRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados("El perfil con este id no fue encontrado");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Perfil perfil = perfilRepository.getReferenceById(id);
        return new DatosRespuestaPerfil(perfil);
    }


    // Actualizar un perfil (nombre)
    // Permitido al administrador
    public DatosRespuestaPerfil actualizarPerfil (DatosActualizarPerfil datosActualizarPerfil) {

        if (!perfilRepository.existsById(datosActualizarPerfil.id())) {
            throw new ValidationDeDatosIngresados("El perfil con este id no fue encontrado");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Perfil perfil = perfilRepository.getReferenceById(datosActualizarPerfil.id());

        perfil.actualizarDatos(datosActualizarPerfil);
        return new DatosRespuestaPerfil(perfil);
    }


    // Eliminar un perfil, permitido al administrador
    // Antes eliminarlo de todos los usuarios.
    // Perfiles USUARIO y ADMINISTRADOR no se pueden eliminar
    public void eliminarPerfil (Long id){

        if (!perfilRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados ("El perfil con este id no fue encontrado");
        }

        // Los perfiles "USUARIO" y "ADMINISTRADOR" no se pueden eliminar
        if (perfilRepository.getReferenceById(id).getNombre().equals("USUARIO")
                || perfilRepository.getReferenceById(id).getNombre().equals("ADMINISTRADOR")){
            throw new ValidationDeDatosIngresados("Este perfil no se puede eliminar");
        }

        // Permiso si el usuario es administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        // Eliminar este perfil de todos los usuarios que lo tienen
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepository.seleccionaUsuariosConPerfil(id);
        usuarios.forEach(u -> u.getPerfiles().removeIf(per -> per.getId().equals(id)));

        perfilRepository.deleteById(id);
    }

}
