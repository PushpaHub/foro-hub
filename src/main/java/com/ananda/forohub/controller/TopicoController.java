package com.ananda.forohub.controller;

import com.ananda.forohub.domain.topico.*;
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
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;
    @Autowired
    private TopicoRepository topicoRepository;


    @PostMapping
    @Transactional
    // Registrar un nuevo tópico, abierto a todos
    public ResponseEntity registrarTopico
            (@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
             UriComponentsBuilder uriComponentsBuilder){

        var response = topicoService.registrar(datosRegistroTopico);
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(url).body(response);
    }

    @GetMapping
    // Listar tópicos, permitido a todos los perfiles
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos
            (@PageableDefault(size = 10) Pageable paginacion) {

        var response = topicoService.listarTopicos(paginacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    // Listar solo un tópico con la id proporcionada, permitido a todos los perfiles
    public ResponseEntity<DatosRespuestaTopico> retornaDatosTopico(@PathVariable Long id) {

        var response = topicoService.listarUnTopico(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Transactional
    // Actualizar tópico, se puede: titulo, mensaje, estatus, curso
    // Permitido al administrador, al moderador y al usuario solo si es autor del tópico
    public ResponseEntity actualizarTopico(
            @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {

        var response = topicoService.actualizarTopico(datosActualizarTopico);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    // Eliminar un tópico
    // Permitido al administrador, al moderador y al usuario solo si es autor del tópico
    public ResponseEntity eliminarTopico(@PathVariable Long id){

        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }

}

