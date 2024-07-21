package com.ananda.forohub.domain.perfil;

import com.ananda.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Table(name = "perfiles")
@Entity(name = "Perfil")
//Lombok para getters, setters y constructores
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @ManyToMany(mappedBy = "perfiles")
    private Set<Usuario> usuarios = new HashSet<>();


    public Perfil(DatosRegistroPerfil datosRegistroPerfil) {
        this.nombre = datosRegistroPerfil.nombre();
    }

    public void actualizarDatos(DatosActualizarPerfil datosActualizarPerfil){
        if (datosActualizarPerfil.nombre() != null)
            this.nombre = datosActualizarPerfil.nombre();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + nombre;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

}
