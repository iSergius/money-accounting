package name.isergius.finance.personal.damain;

import name.isergius.finance.personal.damain.entity.Record;
import name.isergius.finance.personal.damain.entity.RecordId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public interface RecordInteractor {

    Mono<RecordId> generateId();

    Mono<Void> save(Mono<Record> record);

    Mono<Record> getBy(Mono<UUID> id);

    Flux<Record> getAll();
}
