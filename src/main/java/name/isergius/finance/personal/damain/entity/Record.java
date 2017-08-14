package name.isergius.finance.personal.damain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sergey Kondratyev
 */
public class Record {
    private UUID id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime date;

    public Record(UUID id, BigDecimal amount, String currency, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
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
