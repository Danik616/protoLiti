package com.spring.prototipolitigando.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.spring.prototipolitigando.entity.RolEntity;

public interface IRolRepository extends R2dbcRepository<RolEntity, Long>{
    
}
