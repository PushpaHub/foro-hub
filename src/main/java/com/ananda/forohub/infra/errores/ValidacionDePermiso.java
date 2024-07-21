package com.ananda.forohub.infra.errores;

import com.ananda.forohub.domain.usuario.GetRolesDeUsuario;
import com.ananda.forohub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidacionDePermiso {

    @Autowired
    private GetRolesDeUsuario getRolesDeUsuario;

    public void validarPermiso (Set<String> rolesPermitidos, Long id){
        // Set rolesPermitidos incluye roles en el formato "ROLE_XXXXX"
        // id incluye id del usuario que se está modificandodo,
        // si el usuario actual tiene derecho de modificar los datos de si mismo.
        // Si este permiso no existe, id=null

        // Obtener el id del usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Long usuarioActual = usuarioAutenticado.getId();

        // Si el usuarioActual no tiene uno de rolesPermitidos, no progresa
        Set<String> roles = getRolesDeUsuario.getRoles(usuarioActual);

        // Comprobar si el usuario actual tiene al menos uno de los roles permitidos
        boolean tieneRolPermitido = roles.stream().anyMatch(rolesPermitidos::contains);

        // Comprobar si el id proporcionado no es null y coincide con el id del usuario actual
        boolean esIdValido = id != null && id.equals(usuarioActual);

        // Si el usuario no tiene un rol permitido y el id no es válido, lanzar excepción
        if (!tieneRolPermitido && !esIdValido) {
            throw new ValidationDeRoles("No tienes permiso para ejecutar esta acción");
        }

    }
}
