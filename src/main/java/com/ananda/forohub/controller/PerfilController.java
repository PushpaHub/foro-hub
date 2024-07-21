package com.ananda.forohub.controller;

import com.ananda.forohub.domain.perfil.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;


    @PostMapping
    @Transactional
    // Registrar un nuevo perfil, solo permitido al administrador
    public ResponseEntity registrarPerfil (
            @RequestBody @Valid DatosRegistroPerfil datosRegistroPerfil){

        var response = perfilService.registrarPerfil(datosRegistroPerfil);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    // Listar perfiles, solo permitido al administrador
    public ResponseEntity<Page<DatosListadoPerfil>> listadoPerfiles
            (@PageableDefault(size = 10) Pageable paginacion) {

        var response = perfilService.listarPerfiles(paginacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    // Listar solo un perfil con la id proporcionada
    // Permitido al administrador
    public ResponseEntity<DatosRespuestaPerfil> retornaDatosPerfil(@PathVariable Long id) {

        var response = perfilService.listarUnPerfil(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping
    @Transactional
    // Actualizar un perfil (nombre)
    // Permitido al administrador
    public ResponseEntity<DatosRespuestaPerfil> ActualizarPerfil(
            @RequestBody @Valid DatosActualizarPerfil datosActualizarPerfil) {

        var response = perfilService.actualizarPerfil(datosActualizarPerfil);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @Transactional
    // Eliminar un perfil, permitido al administrador
    // Ante eliminarlo de todos los usuarios y ningún usuario debería quedarse sin perfiles
    public ResponseEntity eliminarPerfil(@PathVariable Long id){

        perfilService.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }

}

