package com.spring.prototipolitigando.repository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;

import com.spring.prototipolitigando.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface IUserRepository extends R2dbcRepository<UserEntity, Long>{

    @Query("SELECT * FROM user WHERE email = :email")
    public Mono<UserEntity> findByEmail(String email);
}
