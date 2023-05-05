package com.spring.prototipolitigando.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PrincipalRoutes {
    
    @Bean
    public RouterFunction<ServerResponse> routeInit(PrincipalHandler principalHandler){
        return RouterFunctions.route(GET("/"), principalHandler::listarUsuarios);
    }
}
