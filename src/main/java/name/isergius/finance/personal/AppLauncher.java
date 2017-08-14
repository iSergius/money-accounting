package name.isergius.finance.personal;

import name.isergius.finance.personal.damain.PingHandler;
import name.isergius.finance.personal.damain.PingInteractor;
import name.isergius.finance.personal.damain.RecordHandler;
import name.isergius.finance.personal.damain.RecordInteractor;
import name.isergius.finance.personal.damain.entity.Record;
import name.isergius.finance.personal.ui.dto.RecordIdResource;
import name.isergius.finance.personal.ui.dto.RecordResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.HttpSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Sergey Kondratyev
 */

@SpringBootApplication
public class AppLauncher {

    public static void main(String[] args) {
        SpringApplication.run(AppLauncher.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routerPing(PingInteractor interactor) {
        return route(GET("/ping"), request -> ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(interactor.ping(), String.class));
    }

    @Bean
    public RouterFunction<ServerResponse> routerRecord(RecordInteractor interactor) {
        return route(GET("/record/generateId"), request ->
                interactor.generateId()
                        .map(recordId -> Mono.just(new RecordIdResource(recordId.getId(), recordId.getDate())))
                        .flatMap(recordIdResource -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(recordIdResource, RecordIdResource.class))
        ).andRoute(PUT("/record/{id}"), request ->
                request.bodyToMono(RecordResource.class)
                        .map(r -> {
                            UUID id = UUID.fromString(request.pathVariable("id"));
                            BigDecimal amount = new BigDecimal(r.getAmount());
                            LocalDateTime date = LocalDateTime.ofInstant(new Date(r.getDate()).toInstant(), ZoneId.systemDefault());
                            return Mono.just(new Record(id, amount, r.getCurrency(), date));
                        })
                        .flatMap(interactor::save)
                        .then(ServerResponse.created(request.uri()).build())
                        .onErrorReturn(ServerResponse.badRequest().build().block())
        );
    }

    @Bean
    public PingInteractor pingHandler() {
        return new PingHandler();
    }

    @Bean
    public RecordInteractor recordHandler() {
        return new RecordHandler();
    }

    @Configuration
    @EnableWebFluxSecurity
    class SecurityConfiguration {

        @Bean
        UserDetailsRepository userDetailsRepository() {
            return new MapUserDetailsRepository(
                    User.withUsername("u3er").roles("USER").password("password").build(),
                    User.withUsername("4dmin").roles("ADMIN", "USER").password("password").build());
        }

        @Bean
        SecurityWebFilterChain securityWebFilterChain(HttpSecurity httpSecurity) {
            return httpSecurity
                    .authorizeExchange()
                    .pathMatchers(HttpMethod.GET, "/ping").permitAll()
                    .pathMatchers("/record/**").hasRole("USER")
                    .anyExchange().hasRole("ADMIN").and()
                    .build();

        }
    }
}
