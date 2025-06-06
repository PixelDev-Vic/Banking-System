import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankingSystem {
    private static final String ADMIN_PASSWORD = "admin123";
    private Map<String, Customer> customers;
    private AccountService accountService;
    private TransactionService transactionService;
    private FileManager fileManager;

    public BankingSystem() {
        this.customers = new HashMap<>();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.fileManager = new FileManager();
        loadData();
    }

    public boolean adminLogin(String password) {
        return ADMIN_PASSWORD.equals(password);
    }

    public String createCustomer(String name, String password, String accountType, double initialDeposit) {
        try {
            String accountNumber = generateAccountNumber();
            Account account = new Account(accountNumber, name, accountType, initialDeposit);
            Customer customer = new Customer(name, password, account);

            customers.put(accountNumber, customer);

            // Record initial deposit transaction
            if (initialDeposit > 0) {
                Transaction transaction = new Transaction(
                        accountNumber,
                        "DEPOSIT",
                        initialDeposit,
                        "Initial deposit"
                );
                transactionService.addTransaction(transaction);
            }

            saveData();
            return accountNumber;
        } catch (Exception e) {
            System.out.println("Error creating customer: " + e.getMessage());
            return null;
        }
    }

    public Customer customerLogin(String accountNumber, String password) {
        Customer customer = customers.get(accountNumber);
        if (customer != null && customer.validatePassword(password)) {
            if (!customer.getAccount().isActive()) {
                System.out.println("Account is suspended. Please contact admin.");
                return null;
            }
            return customer;
        }
        return null;
    }

    public boolean deposit(String accountNumber, double amount) {
        Customer customer = customers.get(accountNumber);
        if (customer != null && customer.getAccount().isActive()) {
            customer.getAccount().deposit(amount);

            Transaction transaction = new Transaction(
                    accountNumber,
                    "DEPOSIT",
                    amount,
                    "Cash deposit"
            );
            transactionService.addTransaction(transaction);

            saveData();
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Customer customer = customers.get(accountNumber);
        if (customer != null && customer.getAccount().isActive()) {
            if (customer.getAccount().withdraw(amount)) {
                Transaction transaction = new Transaction(
                        accountNumber,
                        "WITHDRAWAL",
                        amount,
                        "Cash withdrawal"
                );
                transactionService.addTransaction(transaction);

                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean transfer(String fromAccount, String toAccount, double amount) {
        Customer fromCustomer = customers.get(fromAccount);
        Customer toCustomer = customers.get(toAccount);

        if (fromCustomer != null && toCustomer != null &&
                fromCustomer.getAccount().isActive() && toCustomer.getAccount().isActive()) {

            if (fromCustomer.getAccount().withdraw(amount)) {
                toCustomer.getAccount().deposit(amount);

                // Record withdrawal transaction
                Transaction withdrawalTransaction = new Transaction(
                        fromAccount,
                        "TRANSFER_OUT",
                        amount,
                        "Transfer to " + toAccount
                );
                transactionService.addTransaction(withdrawalTransaction);

                // Record deposit transaction
                Transaction depositTransaction = new Transaction(
                        toAccount,
                        "TRANSFER_IN",
                        amount,
                        "Transfer from " + fromAccount
                );
                transactionService.addTransaction(depositTransaction);

                saveData();
                return true;
            }
        }
        return false;
    }

    public void viewAllCustomers() {
        System.out.println("\n=== ALL CUSTOMERS ===");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.printf("%-15s %-20s %-15s %-15s %-10s%n",
                "Account No", "Name", "Type", "Balance", "Status");
        System.out.println("=".repeat(80));

        for (Customer customer : customers.values()) {
            Account account = customer.getAccount();
            System.out.printf("%-15s %-20s %-15s $%-14.2f %-10s%n",
                    account.getAccountNumber(),
                    customer.getName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.isActive() ? "Active" : "Suspended"
            );
        }
    }

    public void viewAllTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        List<Transaction> transactions = transactionService.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf("%-15s %-20s %-15s %-15s %-30s%n",
                "Account No", "Date/Time", "Type", "Amount", "Description");
        System.out.println("=".repeat(100));

        for (Transaction transaction : transactions) {
            System.out.printf("%-15s %-20s %-15s $%-14.2f %-30s%n",
                    transaction.getAccountNumber(),
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );
        }
    }

    public boolean deleteCustomer(String accountNumber) {
        if (customers.containsKey(accountNumber)) {
            customers.remove(accountNumber);
            saveData();
            return true;
        }
        return false;
    }

    public boolean toggleAccountStatus(String accountNumber) {
        Customer customer = customers.get(accountNumber);
        if (customer != null) {
            Account account = customer.getAccount();
            account.setActive(!account.isActive());
            saveData();
            return true;
        }
        return false;
    }

    public void viewTransactionHistory(String accountNumber) {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.printf("%-20s %-15s %-15s %-30s%n",
                "Date/Time", "Type", "Amount", "Description");
        System.out.println("=".repeat(85));

        for (Transaction transaction : transactions) {
            System.out.printf("%-20s %-15s $%-14.2f %-30s%n",
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );
        }
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    private void loadData() {
        customers = fileManager.loadCustomers();
        transactionService.setTransactions(fileManager.loadTransactions());
    }

    private void saveData() {
        fileManager.saveCustomers(customers);
        fileManager.saveTransactions(transactionService.getAllTransactions());
    }
}