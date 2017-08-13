package name.isergius.finance.personal.damain;

import name.isergius.finance.personal.damain.entity.Record;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public interface RecordInteractor {

    Mono<UUID> generateId();

    Mono<Void> save(Mono<Record> record);
}
