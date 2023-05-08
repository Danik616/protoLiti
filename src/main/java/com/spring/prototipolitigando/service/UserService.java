package com.spring.prototipolitigando.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetails;
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
    public Mono<ReactiveUserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> new User(userEntity.getEmail(), 
                    userEntity.getPassword(),
                    userEntity.getRole()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                );
    }

    

    
}
