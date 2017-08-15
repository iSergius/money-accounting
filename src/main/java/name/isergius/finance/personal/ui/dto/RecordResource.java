package name.isergius.finance.personal.ui.dto;

import java.util.Objects;
import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public class RecordResource {

    private UUID id;
    private String amount;
    private String currency;
    private long date;

    public RecordResource() {
    }

    public RecordResource(UUID id, String amount, String currency, long date) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public long getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordResource that = (RecordResource) o;
        return date == that.date &&
                Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, currency, date);
    }

    @Override
    public String toString() {
        return "RecordResource{" +
                "id=" + id +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", date=" + date +
                '}';
    }
}
