package name.isergius.finance.personal.ui.dto;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public class RecordIdResource {
    private UUID id;
    private long ttl;

    public RecordIdResource() {
    }

    public RecordIdResource(UUID id, long ttl) {
        this.id = id;
        this.ttl = ttl;
    }

    public UUID getId() {
        return id;
    }

    public long getTtl() {
        return ttl;
    }
}
