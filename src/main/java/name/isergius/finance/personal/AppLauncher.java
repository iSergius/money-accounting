package name.isergius.finance.personal;

import name.isergius.finance.personal.damain.PingHandler;
import name.isergius.finance.personal.damain.PingInteractor;
import name.isergius.finance.personal.damain.RecordHandler;
import name.isergius.finance.personal.damain.RecordInteractor;
import name.isergius.finance.personal.damain.entity.Record;
import name.isergius.finance.personal.data.RecordRepository;
import name.isergius.finance.personal.data.RecordRepositoryImpl;
import name.isergius.finance.personal.ui.dto.RecordIdResource;
import name.isergius.finance.personal.ui.dto.RecordResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
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
                            Instant date = Instant.ofEpochMilli(r.getDate());
                            return Mono.just(new Record(id, amount, r.getCurrency(), date));
                        })
                        .flatMap(interactor::save)
                        .then(ServerResponse.created(request.uri()).build())
                        .onErrorReturn(ServerResponse.badRequest().build().block())
        ).andRoute(GET("/record/{id}"), request ->
                Mono.just(request.pathVariable("id"))
                        .map(UUID::fromString)
                        .map(Mono::just)
                        .flatMap(interactor::getBy)
                        .map(r -> {
                            UUID id = r.getId();
                            String currency = r.getCurrency();
                            String amount = r.getAmount().toString();
                            long date = r.getDate().toEpochMilli();
                            return Mono.just(new RecordResource(id, amount, currency, date));
                        })
                        .flatMap(r -> ServerResponse.ok()
                                .body(r, RecordResource.class))
                        .switchIfEmpty(ServerResponse.notFound()
                                .build())
        ).andRoute(GET("/record/"), request ->
                ServerResponse.ok()
                        .body(interactor.getAll()
                                .map(r -> {
                                    UUID id = r.getId();
                                    String currency = r.getCurrency();
                                    String amount = r.getAmount().toString();
                                    long date = r.getDate().toEpochMilli();
                                    return new RecordResource(id, amount, currency, date);
                                }), RecordResource.class)
        ).andRoute(DELETE("/record/{id}"), request ->
                Mono.just(request.pathVariable("id"))
                        .flatMap(s -> interactor.delBy(Mono.just(UUID.fromString(s))))
                        .then(ServerResponse.ok().build())
        );
    }

    @Bean
    public PingInteractor pingHandler() {
        return new PingHandler();
    }

    @Bean
    public RecordInteractor recordHandler(RecordRepository repository) {
        return new RecordHandler(repository);
    }

    @Bean
    public RecordRepository recordRepository(ReactiveMongoTemplate template) {
        return new RecordRepositoryImpl(template);
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
