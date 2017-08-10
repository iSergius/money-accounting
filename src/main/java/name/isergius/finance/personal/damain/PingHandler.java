package name.isergius.finance.personal.damain;

import reactor.core.publisher.Mono;


/**
 * Sergey Kondratyev
 */

public class PingHandler implements PingInteractor {

    @Override
    public Mono<String> ping() {
        return Mono.just("pong");
    }
}
