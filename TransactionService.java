import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Comparator;

public class TransactionService {
    private List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        // Return transactions sorted by timestamp (newest first)
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactions.stream()
                .filter(t -> t.getTimestamp().isAfter(startDate) && t.getTimestamp().isBefore(endDate))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByAccountAndDateRange(String accountNumber,
                                                                  LocalDateTime startDate,
                                                                  LocalDateTime endDate) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getTimestamp().isAfter(startDate) && t.getTimestamp().isBefore(endDate))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public double getTotalDeposits(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> "DEPOSIT".equals(t.getType()) || "TRANSFER_IN".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalWithdrawals(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> "WITHDRAWAL".equals(t.getType()) || "TRANSFER_OUT".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public int getTransactionCount(String accountNumber) {
        return (int) transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .count();
    }

    public Transaction getLastTransaction(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .max(Comparator.comparing(Transaction::getTimestamp))
                .orElse(null);
    }

    public List<Transaction> getRecentTransactions(String accountNumber, int limit) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public boolean deleteTransaction(String transactionId) {
        return transactions.removeIf(t -> t.getTransactionId().equals(transactionId));
    }

    public Transaction getTransactionById(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions != null ? transactions : new ArrayList<>();
    }

    public void clearTransactions() {
        transactions.clear();
    }

    public void printTransactionSummary(String accountNumber) {
        List<Transaction> accountTransactions = getTransactionsByAccount(accountNumber);
        double totalDeposits = getTotalDeposits(accountNumber);
        double totalWithdrawals = getTotalWithdrawals(accountNumber);
        int transactionCount = getTransactionCount(accountNumber);

        System.out.println("\n=== TRANSACTION SUMMARY ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Total Transactions: " + transactionCount);
        System.out.println("Total Deposits: $" + String.format("%.2f", totalDeposits));
        System.out.println("Total Withdrawals: $" + String.format("%.2f", totalWithdrawals));
        System.out.println("Net Amount: $" + String.format("%.2f", (totalDeposits - totalWithdrawals)));
    }
}