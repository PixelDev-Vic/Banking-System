import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private LocalDateTime timestamp;
    private String type;
    private double amount;
    private String description;

    public Transaction(String accountNumber, String type, double amount, String description) {
        this.accountNumber = accountNumber;
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountNumber='" + accountNumber + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}
