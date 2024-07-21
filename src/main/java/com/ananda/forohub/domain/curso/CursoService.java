package com.ananda.forohub.domain.curso;

import com.ananda.forohub.domain.topico.TopicoRepository;
import com.ananda.forohub.infra.errores.ValidacionDePermiso;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private ValidacionDePermiso validacionDePermiso;


    // Registrar un nuevo curso, permitido al moderador y administrador
    public DatosRespuestaCurso registrarCurso (DatosRegistroCurso datosRegistroCurso){

        if (cursoRepository.existsByNombreAndCategoria
                (datosRegistroCurso.nombre(), datosRegistroCurso.categoria())) {
            throw new ValidationDeDatosIngresados("El curso con el mismo nombre y la misma categoría ya existe");
        }

        // Permiso si el usuario es moderador o administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_MODERADOR", "ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Curso curso = new Curso(datosRegistroCurso);
        cursoRepository.save(curso);
        return new DatosRespuestaCurso(curso);
    }


    // Actualizar un curso, ermitido al administrador y moderador
    public DatosRespuestaCurso actualizarCurso (DatosActualizarCurso datosActualizarCurso) {

        if (!cursoRepository.existsById(datosActualizarCurso.id())) {
            throw new ValidationDeDatosIngresados("El curso con este id no fue encontrado");
        }

        // Permiso si el usuario es moderador o administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_MODERADOR", "ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        Curso curso = cursoRepository.getReferenceById(datosActualizarCurso.id());

        curso.actualizarDatos(datosActualizarCurso);
        return new DatosRespuestaCurso(curso);
    }


    // Eliminar un curso, permitido al administrador y moderador
    // Antes eliminar todos los tópicos que se refieren a este curso
    public void eliminarCurso (Long id){

        if (!cursoRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados ("El curso con este id no fue encontrado");
        }

        // Permiso si el usuario es moderador o administrador
        Set<String> rolesPermitidos = new HashSet<>
                (Set.of("ROLE_MODERADOR", "ROLE_ADMINISTRADOR"));
        Long idPermiso = null;
        validacionDePermiso.validarPermiso(rolesPermitidos, idPermiso);

        // Eliminar todos los tópicos que se refieren a este curso y eliminar el curso
        topicoRepository.deleteByCursoId(id);
        cursoRepository.deleteById(id);

    }

}
