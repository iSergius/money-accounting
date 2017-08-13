package name.isergius.finance.personal.damain;

import name.isergius.finance.personal.damain.entity.Record;
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

    @Override
    public Mono<Void> save(Mono<Record> record) {
        return null;
    }
}
