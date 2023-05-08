package com.spring.prototipolitigando.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.spring.prototipolitigando.repository.IUserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService{
    
    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().toArray(new String[0]))
                .build()
                ).map(UserDetails -> (UserDetails) UserDetails);
    }

    

    
}
