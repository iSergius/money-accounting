package name.isergius.finance.personal.damain;

import name.isergius.finance.personal.damain.entity.Record;
import name.isergius.finance.personal.damain.entity.RecordId;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Sergey Kondratyev
 */

public class RecordHandler implements RecordInteractor {

    private Map<UUID, RecordId> generatedIds = new HashMap<>();

    @Override
    public Mono<RecordId> generateId() {
        return Mono.just(new RecordId(UUID.randomUUID(), Instant.now().plusMillis(2000).toEpochMilli()))
                .doOnNext(recordId -> generatedIds.put(recordId.getId(), recordId));
    }

    @Override
    public Mono<Void> save(Mono<Record> record) {
        return record.filter(r -> generatedIds.containsKey(r.getId()))
                .filter(r -> Instant.now().toEpochMilli() < generatedIds.getOrDefault(r.getId(), new RecordId(null, 0)).getDate())
                .doOnSuccess(System.out::println)
                .switchIfEmpty(Mono.error(new IllegalStateException()))
                .then();
    }
}
