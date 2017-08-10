package name.isergius.finance.personal.ui;

import name.isergius.finance.personal.ui.dto.RecordIdResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

/**
 * Sergey Kondratyev
 */

public class RecordHandler {

    public Mono<ServerResponse> generateId(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new RecordIdResource(UUID.randomUUID(), Duration.ofSeconds(2).toMillis())), RecordIdResource.class);
    }
}
