package com.spring.prototipolitigando.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.spring.prototipolitigando.entity.RolEntity;
import com.spring.prototipolitigando.entity.UserRole;
import com.spring.prototipolitigando.repository.IRolRepository;
import com.spring.prototipolitigando.repository.IUserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService{
    
    private IUserRepository userRepository;

    private IRolRepository rolRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
public Mono<UserDetails> findByUsername(String email) {
    return userRepository.findByEmail(email)
            .flatMap(userEntity -> {
                List<Long> roleIds = userEntity.getUserRoles().stream()
                        .map(UserRole::getRoleId)
                        .collect(Collectors.toList());

                return rolRepository.findAllById(roleIds)
                        .collectList()
                        .map(roles -> {
                            Map<Long, RolEntity> rolesMap = roles.stream()
                                    .collect(Collectors.toMap(RolEntity::getId, Function.identity()));

                            List<String> roleNames = userEntity.getUserRoles().stream()
                                    .map(UserRole::getRoleId)
                                    .map(rolesMap::get)
                                    .filter(Objects::nonNull)
                                    .map(RolEntity::getName)
                                    .collect(Collectors.toList());

                            return User.builder()
                                    .username(userEntity.getEmail())
                                    .password(userEntity.getPassword())
                                    .roles(roleNames.toArray(new String[0]))
                                    .build();
                        });
            })
            .cast(UserDetails.class);
}



    

    
}
