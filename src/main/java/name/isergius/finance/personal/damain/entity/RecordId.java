package name.isergius.finance.personal.damain.entity;

import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public class RecordId {

    private UUID id;
    private long date;

    public RecordId(UUID id, long date) {
        this.id = id;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "RecordId{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
