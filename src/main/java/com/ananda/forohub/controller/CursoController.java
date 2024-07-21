package com.ananda.forohub.controller;

import com.ananda.forohub.domain.curso.Curso;
import com.ananda.forohub.domain.curso.CursoRepository;
import com.ananda.forohub.domain.curso.DatosRegistroCurso;
import com.ananda.forohub.domain.curso.DatosRespuestaCurso;
import com.ananda.forohub.domain.curso.*;
import com.ananda.forohub.infra.errores.ValidationDeDatosIngresados;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private CursoService cursoService;

    @PostMapping
    @Transactional
    public ResponseEntity registrarCurso (@RequestBody @Valid DatosRegistroCurso datosRegistroCurso,
                                          UriComponentsBuilder uriComponentsBuilder){

        var response = cursoService.registrarCurso(datosRegistroCurso);

        URI url = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(url).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoCurso>> listadoCursos
            (@PageableDefault(size = 10) Pageable paginacion) {

        return ResponseEntity.ok(cursoRepository.findAll(paginacion)
                .map(DatosListadoCurso::new));
    }

    @GetMapping("/{id}")
    // Listar solo un curso con la id proporcionada
    public ResponseEntity<DatosRespuestaCurso> retornaDatosCurso(@PathVariable Long id) {

        if (!cursoRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados ("El curso con este id no fue encontrado");
        }

        Curso curso = cursoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaCurso(curso));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarCurso(@RequestBody @Valid DatosActualizarCurso datosActualizarCurso) {

        var response = cursoService.actualizarCurso(datosActualizarCurso);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarCurso(@PathVariable Long id){

        if (!cursoRepository.existsById(id)) {
            throw new ValidationDeDatosIngresados ("El curso con este id no fue encontrado");
        }

        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }

}

