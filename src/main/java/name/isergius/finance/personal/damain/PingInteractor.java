package name.isergius.finance.personal.damain;

import reactor.core.publisher.Mono;

/**
 * Sergey Kondratyev
 */
public interface PingInteractor {

    Mono<String> ping();

}
