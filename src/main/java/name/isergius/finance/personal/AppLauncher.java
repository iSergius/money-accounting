package name.isergius.finance.personal;

import name.isergius.finance.personal.ui.PingHandler;
import name.isergius.finance.personal.ui.PingInteractor;
import name.isergius.finance.personal.ui.RecordHandler;
import name.isergius.finance.personal.ui.RecordInteractor;
import name.isergius.finance.personal.ui.dto.RecordIdResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
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
    public RouterFunction<ServerResponse> monoRouterPing(PingInteractor pingInteractor, RecordInteractor recordInteractor) {
        return route(GET("/ping"), request -> {
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(pingInteractor.ping(), String.class);
        }).andRoute(GET("/record/generateId"), request -> {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(recordInteractor.generateId()
                            .map(uuid -> new RecordIdResource(uuid, Duration.ofSeconds(2).toMillis())), RecordIdResource.class);
        });
    }

    @Bean
    public PingHandler pingHandler() {
        return new PingHandler();
    }

    @Bean
    public RecordHandler recordHandler() {
        return new RecordHandler();
    }
}
