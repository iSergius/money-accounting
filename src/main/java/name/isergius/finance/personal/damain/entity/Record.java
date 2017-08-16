package name.isergius.finance.personal.damain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Sergey Kondratyev
 */
@Document
public class Record {

    @Id
    private UUID id;
    private BigDecimal amount;
    private String currency;
    private Instant date;

    public Record() {
    }

    public Record(UUID id, BigDecimal amount, String currency, Instant date) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(id, record.id) &&
                Objects.equals(amount, record.amount) &&
                Objects.equals(currency, record.currency) &&
                Objects.equals(date, record.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, currency, date);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", date=" + date +
                '}';
    }
}
