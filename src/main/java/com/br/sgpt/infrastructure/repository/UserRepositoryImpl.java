package com.br.sgpt.infrastructure.repository;

import com.br.sgpt.domain.entity.User;
import com.br.sgpt.domain.repository.UserRepository;
import com.br.sgpt.infrastructure.jpa.JpaUserRepository;
import com.br.sgpt.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaRepo;

    public UserRepositoryImpl(JpaUserRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.jpaRepo.findByUsername(username)
                .map(e -> new User(e.getId(), e.getUsername(), e.getPassword(), e.getEmail(), e.getRole()));
    }

    @Override
    public Optional<User> findByEmail(String username) {
        return this.jpaRepo.findByEmail(username)
                .map(e -> new User(e.getId(), e.getUsername(), e.getPassword(), e.getEmail(), e.getRole()));
    }

    @Override
    public void save(User user) {
        this.jpaRepo.save(new UserEntity(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole()));
    }
}