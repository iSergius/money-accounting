package name.isergius.finance.personal.damain;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public interface RecordInteractor {

    Mono<UUID> generateId();
}
