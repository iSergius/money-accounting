package name.isergius.finance.personal.data;

import name.isergius.finance.personal.damain.entity.Record;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public interface RecordRepository {

    Mono<Record> save(Record entity);

    Mono<Record> findById(Publisher<UUID> id);
}
