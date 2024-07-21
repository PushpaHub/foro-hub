package com.ananda.forohub.domain.curso;

import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActualizarCursoService {

    @Autowired
    private CursoRepository cursoRepository;


    public DatosRespuestaCurso actualizar (DatosActualizarCurso datosActualizarCurso){

        if (!cursoRepository.existsById(datosActualizarCurso.id())) {
            throw new ValidationDeDatosIngresados ("El curso con este id no fue encontrado");
        }

        Curso curso = cursoRepository.getReferenceById(datosActualizarCurso.id());

        // Checar si es posible nuevo nombre o nueva categoría o ya existe en DB
        String nuevoNombre = curso.getNombre();
        String nuevaCategoria = curso.getCategoria();
        if (datosActualizarCurso.nombre() != null) nuevoNombre = datosActualizarCurso.nombre();
        if (datosActualizarCurso.categoria() != null) nuevaCategoria = datosActualizarCurso.categoria();
        if (cursoRepository.existsByNombreAndCategoriaAndIdNot(
                nuevoNombre, nuevaCategoria, datosActualizarCurso.id())) {
            throw new ValidationDeDatosIngresados("El curso con el mismo nombre y la misma categoría ya existe");
        }

        curso.actualizarDatos(datosActualizarCurso);

        return new DatosRespuestaCurso(curso);
    }

}
