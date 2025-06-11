import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BankingSystem {
    private static final String ADMIN_PASSWORD = "admin123";
    private Map<String, Customer> customers;
    private AccountService accountService;
    private TransactionService transactionService;
    private FileManager fileManager;
    private Scanner scanner;

    public BankingSystem() {
        this.customers = new HashMap<>();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.fileManager = new FileManager();
        this.scanner = new Scanner(System.in);
        loadData();
    }

    public boolean adminLogin(String password) {
        return ADMIN_PASSWORD.equals(password);
    }

    public String createCustomer(String name, String pin, String accountType, double initialDeposit) {
        try {
            // Validate PIN format (4 digits)
            if (!isValidPin(pin)) {
                System.out.println("Invalid PIN! PIN must be exactly 4 digits.");
                return null;
            }

            String accountNumber = generateAccountNumber();
            Account account = new Account(accountNumber, name, accountType, initialDeposit);
            Customer customer = new Customer(name, pin, account);

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
            System.out.println("Account created successfully!");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Name: " + name);
            System.out.println("Account Type: " + accountType);
            System.out.println("Initial Balance: $" + String.format("%.2f", initialDeposit));
            return accountNumber;
        } catch (Exception e) {
            System.out.println("Error creating customer: " + e.getMessage());
            return null;
        }
    }

    public Customer customerLogin(String accountNumber, String pin) {
        Customer customer = customers.get(accountNumber);
        if (customer != null && customer.validatePin(pin)) {
            if (!customer.getAccount().isActive()) {
                System.out.println("Account is suspended. Please contact admin.");
                return null;
            }
            System.out.println("Login successful! Welcome, " + customer.getName());
            return customer;
        }
        System.out.println("Invalid account number or PIN!");
        return null;
    }

    public boolean deposit(String accountNumber, String pin, double amount) {
        // Verify PIN before transaction
        Customer customer = customers.get(accountNumber);
        if (customer == null || !customer.validatePin(pin)) {
            System.out.println("Transaction failed: Invalid account number or PIN!");
            return false;
        }

        if (!customer.getAccount().isActive()) {
            System.out.println("Transaction failed: Account is suspended!");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Transaction failed: Invalid deposit amount!");
            return false;
        }

        customer.getAccount().deposit(amount);

        Transaction transaction = new Transaction(
                accountNumber,
                "DEPOSIT",
                amount,
                "Cash deposit"
        );
        transactionService.addTransaction(transaction);

        saveData();
        System.out.println("Deposit successful!");
        System.out.println("Amount deposited: $" + String.format("%.2f", amount));
        System.out.println("New balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
        return true;
    }

    public boolean withdraw(String accountNumber, String pin, double amount) {
        // Verify PIN before transaction
        Customer customer = customers.get(accountNumber);
        if (customer == null || !customer.validatePin(pin)) {
            System.out.println("Transaction failed: Invalid account number or PIN!");
            return false;
        }

        if (!customer.getAccount().isActive()) {
            System.out.println("Transaction failed: Account is suspended!");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Transaction failed: Invalid withdrawal amount!");
            return false;
        }

        // Check available funds
        if (customer.getAccount().getBalance() < amount) {
            System.out.println("Transaction failed: Insufficient funds!");
            System.out.println("Available balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            return false;
        }

        if (customer.getAccount().withdraw(amount)) {
            Transaction transaction = new Transaction(
                    accountNumber,
                    "WITHDRAWAL",
                    amount,
                    "Cash withdrawal"
            );
            transactionService.addTransaction(transaction);

            saveData();
            System.out.println("Withdrawal successful!");
            System.out.println("Amount withdrawn: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            return true;
        }
        return false;
    }

    public boolean transfer(String fromAccount, String fromPin, String toAccount, double amount) {
        // Verify PIN for source account
        Customer fromCustomer = customers.get(fromAccount);
        if (fromCustomer == null || !fromCustomer.validatePin(fromPin)) {
            System.out.println("Transfer failed: Invalid source account number or PIN!");
            return false;
        }

        Customer toCustomer = customers.get(toAccount);
        if (toCustomer == null) {
            System.out.println("Transfer failed: Destination account not found!");
            return false;
        }

        if (!fromCustomer.getAccount().isActive() || !toCustomer.getAccount().isActive()) {
            System.out.println("Transfer failed: One or both accounts are suspended!");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Transfer failed: Invalid transfer amount!");
            return false;
        }

        // Check available funds
        if (fromCustomer.getAccount().getBalance() < amount) {
            System.out.println("Transfer failed: Insufficient funds!");
            System.out.println("Available balance: $" + String.format("%.2f", fromCustomer.getAccount().getBalance()));
            return false;
        }

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
            System.out.println("Transfer successful!");
            System.out.println("Amount transferred: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", fromCustomer.getAccount().getBalance()));
            return true;
        }
        return false;
    }

    public void checkBalance(String accountNumber, String pin) {
        Customer customer = customers.get(accountNumber);
        if (customer == null || !customer.validatePin(pin)) {
            System.out.println("Balance inquiry failed: Invalid account number or PIN!");
            return;
        }

        if (!customer.getAccount().isActive()) {
            System.out.println("Balance inquiry failed: Account is suspended!");
            return;
        }

        System.out.println("\n=== BALANCE INQUIRY ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + customer.getName());
        System.out.println("Account Type: " + customer.getAccount().getAccountType());
        System.out.println("Current Balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
        System.out.println("Account Status: " + (customer.getAccount().isActive() ? "Active" : "Suspended"));
    }

    public void viewRecentTransactions(String accountNumber, String pin, int limit) {
        Customer customer = customers.get(accountNumber);
        if (customer == null || !customer.validatePin(pin)) {
            System.out.println("Transaction history failed: Invalid account number or PIN!");
            return;
        }

        System.out.println("\n=== RECENT TRANSACTION HISTORY ===");
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        // Show only the most recent transactions (limit)
        int start = Math.max(0, transactions.size() - limit);
        List<Transaction> recentTransactions = transactions.subList(start, transactions.size());

        System.out.printf("%-20s %-15s %-15s %-30s%n",
                "Date/Time", "Type", "Amount", "Description");
        System.out.println("=".repeat(85));

        for (int i = recentTransactions.size() - 1; i >= 0; i--) {
            Transaction transaction = recentTransactions.get(i);
            System.out.printf("%-20s %-15s $%-14.2f %-30s%n",
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );
        }
    }

    // User interface methods
    public void customerInterface() {
        System.out.println("\n=== CUSTOMER BANKING INTERFACE ===");

        while (true) {
            System.out.println("\n1. Create New Account");
            System.out.println("2. Login to Existing Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    createAccountInterface();
                    break;
                case 2:
                    loginInterface();
                    break;
                case 3:
                    System.out.println("Thank you for using our banking system!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void createAccountInterface() {
        System.out.println("\n=== CREATE NEW ACCOUNT ===");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter a 4-digit PIN: ");
        String pin = scanner.nextLine();

        System.out.print("Enter account type (SAVINGS/CHECKING): ");
        String accountType = scanner.nextLine().toUpperCase();

        System.out.print("Enter initial deposit amount: $");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String accountNumber = createCustomer(name, pin, accountType, initialDeposit);

        if (accountNumber != null) {
            System.out.println("\nAccount created successfully!");
            System.out.println("Please save your account number: " + accountNumber);
        }
    }

    private void loginInterface() {
        System.out.println("\n=== LOGIN ===");

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        Customer customer = customerLogin(accountNumber, pin);

        if (customer != null) {
            customerMenu(accountNumber, pin);
        }
    }

    private void customerMenu(String accountNumber, String pin) {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Recent Transactions");
            System.out.println("6. View All Transaction History");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    checkBalance(accountNumber, pin);
                    break;
                case 2:
                    depositInterface(accountNumber, pin);
                    break;
                case 3:
                    withdrawInterface(accountNumber, pin);
                    break;
                case 4:
                    transferInterface(accountNumber, pin);
                    break;
                case 5:
                    viewRecentTransactions(accountNumber, pin, 5);
                    break;
                case 6:
                    viewTransactionHistory(accountNumber);
                    break;
                case 7:
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void depositInterface(String accountNumber, String pin) {
        System.out.print("Enter deposit amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        deposit(accountNumber, pin, amount);
    }

    private void withdrawInterface(String accountNumber, String pin) {
        System.out.print("Enter withdrawal amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        withdraw(accountNumber, pin, amount);
    }

    private void transferInterface(String accountNumber, String pin) {
        System.out.print("Enter destination account number: ");
        String toAccount = scanner.nextLine();

        System.out.print("Enter transfer amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        transfer(accountNumber, pin, toAccount, amount);
    }

    // Helper method to validate PIN format
    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }

    // Admin methods (unchanged)
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

    // Main method for testing
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.customerInterface();
    }
}
