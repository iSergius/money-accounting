package name.isergius.finance.personal.ui;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public interface RecordInteractor {

    Mono<UUID> generateId();
}
