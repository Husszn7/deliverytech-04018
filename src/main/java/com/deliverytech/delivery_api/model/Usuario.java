package com.deliverytech.delivery_api.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.deliverytech.delivery_api.enums.Role;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank(message = "O email é obrigatório")
    private String email;

    private String senha;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword(){
        return senha;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
