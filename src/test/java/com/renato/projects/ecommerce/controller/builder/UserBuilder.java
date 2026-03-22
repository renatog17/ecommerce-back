package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Role;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;

import java.time.Instant;
import java.util.Set;

public class UserBuilder {

    private UserDetailsImpl user;

    public UserBuilder() {
        user = new UserDetailsImpl();
        user.setEmail("user@email.com");
        user.setPassword("123456");
        user.setName("User padrão");
        user.setVerified(true);
    }

    public UserBuilder comEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder comSenha(String senha) {
        user.setPassword(senha);
        return this;
    }

    public UserBuilder naoVerificado() {
        user.setVerified(false);
        return this;
    }

    public UserBuilder comRoles(Set<Role> roles) {
        user.setRoles(roles);
        return this;
    }

    public UserDetailsImpl build() {
        return user;
    }
}