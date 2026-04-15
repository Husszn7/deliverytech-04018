package com.deliverytech.delivery_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository ){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) 
    throws UsernameNotFoundException{
        
        return usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }
    
}
