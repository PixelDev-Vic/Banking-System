import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileManager {
    private static final String DATA_DIR = "data/";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactions.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileManager() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveCustomers(Map<String, Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers.values()) {
                Account account = customer.getAccount();
                writer.println(String.join("|",
                        customer.getName(),
                        customer.getPasswordHash(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        String.valueOf(account.getBalance()),
                        String.valueOf(account.isActive()),
                        account.getCreatedDate().format(DATE_FORMATTER),
                        String.valueOf(account.getInterestRate()),
                        account.getLastInterestCalculation().format(DATE_FORMATTER),
                        String.valueOf(customer.getFailedLoginAttempts()),
                        String.valueOf(customer.isAccountLocked())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    public Map<String, Customer> loadCustomers() {
        Map<String, Customer> customers = new HashMap<>();
        File file = new File(CUSTOMERS_FILE);

        if (!file.exists()) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 11) {
                        String name = parts[0];
                        String passwordHash = parts[1];
                        String accountNumber = parts[2];
                        String accountType = parts[3];
                        double balance = Double.parseDouble(parts[4]);
                        boolean isActive = Boolean.parseBoolean(parts[5]);
                        LocalDateTime createdDate = LocalDateTime.parse(parts[6], DATE_FORMATTER);
                        double interestRate = Double.parseDouble(parts[7]);
                        LocalDateTime lastInterestCalc = LocalDateTime.parse(parts[8], DATE_FORMATTER);
                        int failedAttempts = Integer.parseInt(parts[9]);
                        boolean isLocked = Boolean.parseBoolean(parts[10]);

                        Account account = new Account(accountNumber, name, accountType, balance,
                                isActive, createdDate, interestRate, lastInterestCalc);
                        Customer customer = new Customer(name, passwordHash, account, failedAttempts, isLocked);

                        customers.put(accountNumber, customer);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing customer line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }

        return customers;
    }

    public void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(String.join("|",
                        transaction.getTransactionId(),
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        String.valueOf(transaction.getAmount()),
                        transaction.getTimestamp().format(DATE_FORMATTER),
                        transaction.getDescription(),
                        String.valueOf(transaction.getBalanceAfter())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        String transactionId = parts[0];
                        String accountNumber = parts[1];
                        String type = parts[2];
                        double amount = Double.parseDouble(parts[3]);
                        LocalDateTime timestamp = LocalDateTime.parse(parts[4], DATE_FORMATTER);
                        String description = parts[5];
                        double balanceAfter = Double.parseDouble(parts[6]);

                        Transaction transaction = new Transaction(transactionId, accountNumber, type,
                                amount, timestamp, description, balanceAfter);
                        transactions.add(transaction);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }

    public void backupData() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupDir = DATA_DIR + "backup_" + timestamp + "/";

        File backup = new File(backupDir);
        backup.mkdirs();

        try {
            copyFile(CUSTOMERS_FILE, backupDir + "customers.txt");
            copyFile(TRANSACTIONS_FILE, backupDir + "transactions.txt");
            System.out.println("Data backed up to: " + backupDir);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }

    private void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             PrintWriter writer = new PrintWriter(new FileWriter(destination))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }

    public boolean exportToCSV(String filename, Map<String, Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.println("Account_Number,Customer_Name,Account_Type,Balance,Status,Created_Date");

            // Write customer data
            for (Customer customer : customers.values()) {
                Account account = customer.getAccount();
                writer.println(String.join(",",
                        account.getAccountNumber(),
                        "\"" + customer.getName() + "\"",
                        account.getAccountType(),
                        String.valueOf(account.getBalance()),
                        account.isActive() ? "Active" : "Suspended",
                        account.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                ));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }

    public void clearAllData() {
        File customersFile = new File(CUSTOMERS_FILE);
        File transactionsFile = new File(TRANSACTIONS_FILE);

        if (customersFile.exists()) {
            customersFile.delete();
        }
        if (transactionsFile.exists()) {
            transactionsFile.delete();
        }

        System.out.println("All data files cleared.");
    }
}