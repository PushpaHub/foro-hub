package com.ananda.forohub.controller;

import com.ananda.forohub.domain.respuesta.*;
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
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;
    @Autowired
    private RespuestaRepository respuestaRepository;


    @PostMapping
    @Transactional
    // Registrar una nueva respuesta, permitido a todos los perfiles
    public ResponseEntity registrarRespuesta
            (@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
             UriComponentsBuilder uriComponentsBuilder){

        var response = respuestaService.registrarRespuesta(datosRegistroRespuesta);
        URI url = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(url).body(response);
    }

    @GetMapping
    // Listar respuestas, permitido a todos los perfiles
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoTopicos
            (@PageableDefault(size = 10) Pageable paginacion) {

        var response = respuestaService.listarRespuestas(paginacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    // Listar solo una respuesta con la id proporcionada, permitido a todos los perfiles
    public ResponseEntity<DatosRespuestaRespuesta> retornaDatosRespuesta(@PathVariable Long id) {

        var response = respuestaService.listarUnaRespuesta(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Transactional
    // Actualizar respuesta, se puede: mensaje, solucion
    // Permitido al administrador, al moderador y al usuario solo si es el autor de la respuesta
    public ResponseEntity actualizarRespuesta(
            @RequestBody @Valid DatosActualizarRespuesta datosActualizarRespuesta) {

        var response = respuestaService.actualizarRespuesta(datosActualizarRespuesta);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    // Eliminar una respuesta
    // Permitido al administrador, al moderador y al usuario solo si es el autor de la respuesta
    public ResponseEntity eliminarRespuesta (@PathVariable Long id){

        respuestaService.eliminarRespuesta(id);
        return ResponseEntity.noContent().build();
    }

}

