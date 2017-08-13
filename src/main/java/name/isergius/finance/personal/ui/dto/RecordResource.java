package name.isergius.finance.personal.ui.dto;

/**
 * Sergey Kondratyev
 */
public class RecordResource {

    private String amount;
    private String currency;
    private long date;

    public RecordResource() {
    }

    public RecordResource(String amount, String currency, long date) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
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
}
