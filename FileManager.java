import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public void saveCustomers(Map<String, Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers.values()) {
                Account account = customer.getAccount();
                writer.println(String.format("%s|%s|%s|%s|%.2f|%b",
                        account.getAccountNumber(),
                        customer.getName(),
                        customer.getPin(), // Changed from getPasswordHash() to getPin()
                        account.getAccountType(),
                        account.getBalance(),
                        account.isActive()
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
            return customers; // Return empty map if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    String accountNumber = parts[0];
                    String name = parts[1];
                    String pin = parts[2]; // Changed from passwordHash to pin
                    String accountType = parts[3];
                    double balance = Double.parseDouble(parts[4]);
                    boolean isActive = Boolean.parseBoolean(parts[5]);

                    Account account = new Account(accountNumber, name, accountType, balance);
                    account.setActive(isActive);
                    Customer customer = new Customer(name, pin, account);

                    customers.put(accountNumber, customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing customer data: " + e.getMessage());
        }

        return customers;
    }

    public void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(String.format("%s|%s|%s|%.2f|%s",
                        transaction.getAccountNumber(),
                        transaction.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDescription()
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
            return transactions; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String accountNumber = parts[0];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    String type = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String description = parts[4];

                    Transaction transaction = new Transaction(accountNumber, type, amount, description);
                    // Set the timestamp to the loaded value
                    transaction.setTimestamp(timestamp);
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing transaction data: " + e.getMessage());
        }

        return transactions;
    }

    // Utility method to check if data files exist
    public boolean dataFilesExist() {
        File customersFile = new File(CUSTOMERS_FILE);
        File transactionsFile = new File(TRANSACTIONS_FILE);
        return customersFile.exists() || transactionsFile.exists();
    }

    // Utility method to create backup of data files
    public void backupData() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Backup customers file
            File customersFile = new File(CUSTOMERS_FILE);
            if (customersFile.exists()) {
                File backupCustomers = new File("customers_backup_" + timestamp + ".txt");
                copyFile(customersFile, backupCustomers);
            }

            // Backup transactions file
            File transactionsFile = new File(TRANSACTIONS_FILE);
            if (transactionsFile.exists()) {
                File backupTransactions = new File("transactions_backup_" + timestamp + ".txt");
                copyFile(transactionsFile, backupTransactions);
            }

            System.out.println("Data backup created successfully with timestamp: " + timestamp);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }

    private void copyFile(File source, File destination) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }

    // Method to clear all data (use with caution)
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
