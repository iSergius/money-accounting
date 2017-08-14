package name.isergius.finance.personal.damain;

import name.isergius.finance.personal.damain.entity.Record;
import name.isergius.finance.personal.damain.entity.RecordId;
import reactor.core.publisher.Mono;

/**
 * Sergey Kondratyev
 */
public interface RecordInteractor {

    Mono<RecordId> generateId();

    Mono<Void> save(Mono<Record> record);
}
