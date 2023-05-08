package com.spring.prototipolitigando.routes;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.prototipolitigando.dto.UserRegisterDTO;
import com.spring.prototipolitigando.entity.UserEntity;
import com.spring.prototipolitigando.repository.IRolRepository;
import com.spring.prototipolitigando.repository.IUserRepository;

import reactor.core.publisher.Mono;

@Component
public class PrincipalHandler {

    @Autowired
    public IUserRepository userRepository;

    @Autowired
    public IRolRepository rolRepository;
    
    // private Mono<ServerResponse> response404 = ServerResponse.notFound().build(); 
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> listarUsuarios(ServerRequest request){
        return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userRepository.findAll(), UserEntity.class);
    }

    public Mono<ServerResponse> guardarUsuario(ServerRequest request){
        Mono<UserRegisterDTO> userDtoMono = request.bodyToMono(UserRegisterDTO.class);

        return userDtoMono.flatMap(userDto -> {
            if (!isValidEmail(userDto.getEmail())) {
                return ServerResponse.badRequest().bodyValue("El email es inválido");
            }
            String encryptedPassword = encryptPassword(userDto.getPassword());
            Mono<ServerResponse> role= rolRepository.findById(userDto.getRole())
                .flatMap(rol->
                            {
                        UserEntity userEntity = new UserEntity(userDto.getEmail(), encryptedPassword, Arrays.asList(rol));
                        
                        return userRepository.save(userEntity)
                            .flatMap(savedUser -> ServerResponse.accepted().build())
                            .switchIfEmpty(response406);
                    }
                );
            return role;
        });
    }

    private boolean isValidEmail(String email) {
        // Aquí puedes realizar la validación del email
        // usando un regex u otra técnica
        return true;
    }
    

    private String encryptPassword(String password) {
        // Aquí puedes encriptar la contraseña antes de guardarla en la base de datos
        return password;
    }
    
    
}
