package name.isergius.finance.personal.data;

import name.isergius.finance.personal.damain.entity.Record;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public class RecordRepositoryImpl implements RecordRepository {

    private ReactiveMongoTemplate template;

    public RecordRepositoryImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Record> save(Record entity) {
        return template.save(entity);
    }

    @Override
    public Mono<Record> findById(Publisher<UUID> id) {
        return template.findById(Mono.from(id).block(), Record.class);
    }

    @Override
    public Flux<Record> findAll() {
        return template.findAll(Record.class);
    }

    @Override
    public Mono<Void> deleteBy(Publisher<UUID> id) {
        return Mono.from(id)
                .flatMap(rid -> template.findById(rid, Record.class))
                .switchIfEmpty(Mono.error(new IllegalStateException()))
                .map(template::remove)
                .then();
    }
}
