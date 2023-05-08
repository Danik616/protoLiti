package com.spring.prototipolitigando.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Mono<UserDetails> findByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> (UserDetails) user)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }

    

    
}
