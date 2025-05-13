package com.br.sgpt.infrastructure.persistence;

import com.br.sgpt.domain.entity.Usuario;
import com.br.sgpt.domain.repository.UsuarioRepository;
import com.br.sgpt.infrastructure.persistence.entity.UsuarioEntity;
import com.br.sgpt.infrastructure.persistence.jpa.JpaUsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpa;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return this.jpa.buscarPorEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return this.jpa.buscarPorId(id)
                .map(this::toDomain);
    }

    @Override
    public void salvar(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaHash()
        );

        this.jpa.save(entity);
    }

    @Override
    public void deletar(UUID id) {
        this.jpa.deleteById(id);
    }

    private Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenhaHash()
        );
    }
}