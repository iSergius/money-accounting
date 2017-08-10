package name.isergius.finance.personal.ui;

import reactor.core.publisher.Mono;

/**
 * Sergey Kondratyev
 */
public interface PingInteractor {

    Mono<String> ping();

}
