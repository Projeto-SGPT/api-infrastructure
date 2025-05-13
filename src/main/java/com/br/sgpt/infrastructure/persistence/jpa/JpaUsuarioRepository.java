package com.br.sgpt.infrastructure.persistence.jpa;

import com.br.sgpt.domain.entity.Usuario;
import com.br.sgpt.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> buscarPorId(UUID id);
    Optional<UsuarioEntity> buscarPorEmail(String email);
    void salvar(Usuario usuario);
    void deletar(UUID id);
}
