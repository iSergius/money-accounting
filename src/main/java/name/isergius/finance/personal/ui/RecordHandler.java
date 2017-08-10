package name.isergius.finance.personal.ui;

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
