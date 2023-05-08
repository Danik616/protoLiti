package com.spring.prototipolitigando.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.spring.prototipolitigando.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveCrudRepository<UserEntity, Long>{
    public Mono<UserEntity> findByUsername(String username);
}
