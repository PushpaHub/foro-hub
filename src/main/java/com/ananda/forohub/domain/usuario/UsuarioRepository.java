package com.ananda.forohub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String username);

    Optional<Usuario> findById(Long id);

    boolean existsByLogin(String login);

    @Query("""
            select u from Usuario u
            join u.perfiles p
            where p.id = :id  
            """)
    List<Usuario> seleccionaUsuariosConPerfil(Long id);
}
