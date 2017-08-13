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
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
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
                        .map(uuid -> Mono.just(new RecordIdResource(uuid, Duration.ofSeconds(2).toMillis())))
                        .flatMap(recordIdResource -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(recordIdResource, RecordIdResource.class))
        ).andRoute(PUT("/record/{id}"), request ->
                Mono.from(request.bodyToMono(RecordResource.class)
                        .map(r -> {
                            UUID id = UUID.fromString(request.pathVariable("id"));
                            BigDecimal amount = new BigDecimal(r.getAmount());
                            LocalDateTime date = LocalDateTime.ofInstant(new Date(r.getDate()).toInstant(), ZoneId.systemDefault());
                            return Mono.just(new Record(id, amount, r.getCurrency(), date));
                        })
                        .onErrorResume(throwable -> Mono.empty())
                        .doOnNext(interactor::save))
                        .flatMap(voidMono -> ServerResponse.created(request.uri())
                                .build())
                        .switchIfEmpty(ServerResponse.badRequest()
                                .build())
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
}
