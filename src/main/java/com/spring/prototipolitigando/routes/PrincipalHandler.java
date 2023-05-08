package com.spring.prototipolitigando.routes;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    // private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> listarUsuarios(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userRepository.findAll(), UserEntity.class);
    }

    public Mono<ServerResponse> guardarUsuario(ServerRequest request) {
        Mono<UserRegisterDTO> userDtoMono = request.bodyToMono(UserRegisterDTO.class);

        return userDtoMono.flatMap(userDto -> {
            if (!isValidEmail(userDto.getEmail())) {
                return ServerResponse.badRequest().bodyValue("El email es inválido");
            }
            String encryptedPassword = encryptPassword(userDto.getPassword());
            Mono<ServerResponse> role = rolRepository.findById(userDto.getRole())
                    .flatMap(rol -> {
                        UserEntity userEntity = new UserEntity(userDto.getEmail(), encryptedPassword,
                                Arrays.asList(rol));

                        return userRepository.save(userEntity)
                                .flatMap(savedUser -> ServerResponse.accepted().build())
                                .switchIfEmpty(response406);
                    });
            return role;
        });
    }

    private boolean isValidEmail(String email) {
        String patron = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        // Explicación del patrón:
        // ^ -> Inicio de línea
        // [\\w.-]+ -> Uno o más caracteres alfanuméricos, punto o guión
        // @ -> Símbolo arroba
        // [\\w.-]+ -> Uno o más caracteres alfanuméricos, punto o guión (dominio)
        // \\.[a-zA-Z]{2,} -> Punto y dos o más letras (extensión)
        // $ -> Fin de línea

        return email.matches(patron);
    }

    private String encryptPassword(String password) {
        password = passwordEncoder.encode(password);
        return password;
    }

}
