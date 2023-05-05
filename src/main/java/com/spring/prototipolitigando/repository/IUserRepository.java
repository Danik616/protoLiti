package com.spring.prototipolitigando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.prototipolitigando.entity.userEntity;

public interface IUserRepository extends JpaRepository<userEntity, Long>{
    
}
