package com.ananda.forohub.controller;

import com.ananda.forohub.domain.perfil.DatosActualizarPerfil;
import com.ananda.forohub.domain.perfil.DatosActualizarPerfilesDelUsuario;
import com.ananda.forohub.domain.usuario.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping ("/inscripcion")
    @Transactional
    // Registrar un nuevo usuario, abierto a todos
    public ResponseEntity registrarUsuario (
            @RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario){

        var response = usuarioService.registrar(datosRegistroUsuario);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    // Listar usuarios, solo permitido al administrador
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios
            (@PageableDefault(size = 10) Pageable paginacion) {

        var response = usuarioService.listarUsuarios(paginacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    // Listar solo un usuario con la id proporcionada
    // Permitido al administrador y al mismo usuario
    public ResponseEntity<DatosRespuestaUsuario> retornaDatosUsuario(@PathVariable Long id) {

        var response = usuarioService.listarUnUsuario(id);
        return ResponseEntity.ok(response);
    }


    @PutMapping
    @Transactional
    // Actualizar un usuario, nombre o clave
    // Permitido al administrador y al mismo usuario
    public ResponseEntity<DatosRespuestaUsuario> ActualizarUsuario(
            @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario) {

        var response = usuarioService.actualizarUsuario(datosActualizarUsuario);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @Transactional
    // Eliminar un usuario
    // Permitido al administrador y al mismo usuario
    public ResponseEntity eliminarUsuario(@PathVariable Long id){

        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping ("/anexar_perfil")
    @Transactional
    // Añadir un perfil a un usuario, solo permitido al administrador
    public ResponseEntity AnexarPerfilAlUsuario(@RequestBody @Valid DatosActualizarPerfilesDelUsuario datosAnexarPerfil) {

        var response = usuarioService.anexarPerfil(datosAnexarPerfil);
        return ResponseEntity.ok(response);
    }

    @PutMapping ("/eliminar_perfil")
    @Transactional
    // Eliminar un perfil a un usuario, solo permitido al administrador.
    // El perfil "USUARIO" no se puede eliminar
    public ResponseEntity EliminarPerfilAlUsuario(
            @RequestBody @Valid DatosActualizarPerfilesDelUsuario datosEliminarPerfil) {

        var response = usuarioService.eliminarPerfil(datosEliminarPerfil);
        return ResponseEntity.ok(response);
    }


}

