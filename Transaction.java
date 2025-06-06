import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    private double balanceAfter;

    public Transaction(String accountNumber, String type, double amount, String description) {
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.balanceAfter = 0.0; // Will be set by the calling service
    }

    // Constructor for loading from file
    public Transaction(String transactionId, String accountNumber, String type,
                       double amount, LocalDateTime timestamp, String description, double balanceAfter) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.balanceAfter = balanceAfter;
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', account='%s', type='%s', amount=%.2f, time='%s'}",
                transactionId, accountNumber, type, amount,
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}