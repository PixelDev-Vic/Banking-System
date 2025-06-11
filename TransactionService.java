import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TransactionService class
class TransactionService {
    private List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(String accountNumber,
                                                        java.time.LocalDateTime startDate,
                                                        java.time.LocalDateTime endDate) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getTimestamp().isAfter(startDate) && t.getTimestamp().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public double getTotalDeposits(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType().equals("DEPOSIT") || t.getType().equals("TRANSFER_IN"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalWithdrawals(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType().equals("WITHDRAWAL") || t.getType().equals("TRANSFER_OUT"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public int getTransactionCount(String accountNumber) {
        return getTransactionsByAccount(accountNumber).size();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public void clearTransactions() {
        this.transactions.clear();
    }
}
