package com.ananda.forohub.domain.usuario;

import com.ananda.forohub.domain.perfil.Perfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "usuarios")
@Entity(name = "Usuario")
//Lombok para getters, setters y constructores
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String login; // email
    private String clave;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    )
    private Set<Perfil> perfiles;


    public Usuario (String nombre, String login, String password, Set<Perfil> perfiles){
        this.nombre = nombre;
        this.login = login;
        this.clave = password;
        this.perfiles = perfiles;
    }

    public void actualizarDatos (String nombre, String password) {
        if (nombre != null)
            this.nombre = nombre;
        if (password != null)
            this.clave = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfiles.stream()
                .map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

