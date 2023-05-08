package com.spring.prototipolitigando.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.spring.prototipolitigando.entity.RolEntity;

public interface IRolRepository extends ReactiveCrudRepository<RolEntity, Long>{
    
}
