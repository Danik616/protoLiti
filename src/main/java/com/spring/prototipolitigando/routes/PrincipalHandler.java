package com.spring.prototipolitigando.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.prototipolitigando.entity.userEntity;
import com.spring.prototipolitigando.repository.IUserRepository;

import reactor.core.publisher.Mono;

@Component
public class PrincipalHandler {

    @Autowired
    public IUserRepository userRepository;
    
    private Mono<ServerResponse> response404 = ServerResponse.notFound().build(); 
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> listarUsuarios(ServerRequest request){
        return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userRepository.findAll(), userEntity.class);
    }
    
}
