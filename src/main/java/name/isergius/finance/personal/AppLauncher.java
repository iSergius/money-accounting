package name.isergius.finance.personal;

import name.isergius.finance.personal.ui.PingHandler;
import name.isergius.finance.personal.ui.RecordHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

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
    public RouterFunction<ServerResponse> monoRouterPing(PingHandler pingHandler, RecordHandler recordHandler) {
        return route(GET("/ping"), pingHandler::ping)
                .andRoute(GET("/record/generateId"), recordHandler::generateId);
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
