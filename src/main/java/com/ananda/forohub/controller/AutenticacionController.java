package com.ananda.forohub.controller;

import com.ananda.forohub.domain.usuario.DatosAutenticacionUsuario;
import com.ananda.forohub.domain.usuario.Usuario;
import com.ananda.forohub.infra.security.DatosJWTToken;
import com.ananda.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){
        Authentication authToken = new UsernamePasswordAuthenticationToken
                (datosAutenticacionUsuario.login(), datosAutenticacionUsuario.clave());

        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        Usuario usuarioPrincipal = (Usuario) usuarioAutenticado.getPrincipal();
        var JWTtoken = tokenService.generarToken(usuarioPrincipal);
        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }
}