package name.isergius.finance.personal.damain;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */

public class RecordHandler implements RecordInteractor {

    @Override
    public Mono<UUID> generateId() {
        return Mono.just(UUID.randomUUID());
    }
}
