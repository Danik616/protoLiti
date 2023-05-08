package com.spring.prototipolitigando.repository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.spring.prototipolitigando.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface IUserRepository extends R2dbcRepository<UserEntity, Long>{
    public Mono<UserEntity> findByUsername(String username);
}
