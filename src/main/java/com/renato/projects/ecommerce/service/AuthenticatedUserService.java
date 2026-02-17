package com.renato.projects.ecommerce.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.renato.projects.ecommerce.domain.UserDetailsImpl;

@Service
public class AuthenticatedUserService {

    public UserDetailsImpl getUsuario() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl user) {
            return user;
        }

        throw new RuntimeException("Principal inválido");
    }

    public Long getId() {
        return getUsuario().getId();
    }

    public String getUsername() {
        return getUsuario().getUsername();
    }
}