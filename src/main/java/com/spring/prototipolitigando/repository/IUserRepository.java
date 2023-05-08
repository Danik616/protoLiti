package com.spring.prototipolitigando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.prototipolitigando.entity.userEntity;

import reactor.core.publisher.Mono;

public interface IUserRepository extends JpaRepository<userEntity, Long>{
    public Mono<userEntity> findByUsername(String username);
}
