package br.com.forum_hub.domain.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    private String nomeUsuario;

    private String biografia;

    private String miniBiografia;

    @NotNull
    private String email;

    @NotNull
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getMiniBiografia() {
        return miniBiografia;
    }

}
