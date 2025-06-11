import java.util.Scanner;

public class Main {
    private static BankingSystem bankingSystem = new BankingSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("    WELCOME TO CAVITE STATE UNIVERSITY");
        System.out.println("           BANKING SYSTEM");
        System.out.println("=".repeat(50));

        while (true) {
            showMainMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    adminInterface();
                    break;
                case 2:
                    customerInterface();
                    break;
                case 3:
                    System.out.println("\nThank you for using CvSU Banking System!");
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. Admin Login");
        System.out.println("2. Customer Interface");
        System.out.println("3. Exit");
        System.out.println("=".repeat(40));
    }

    private static void adminInterface() {
        System.out.println("\n=== ADMIN LOGIN ===");
        String password = getStringInputWithBack("Enter admin password (or type 'back' to return): ");
        if (password == null) return; // User chose to go back

        if (bankingSystem.adminLogin(password)) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Invalid admin password!");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("          ADMIN MENU");
            System.out.println("=".repeat(40));
            System.out.println("1. View All Customers");
            System.out.println("2. View All Transactions");
            System.out.println("3. Create Customer Account");
            System.out.println("4. Delete Customer Account");
            System.out.println("5. Toggle Account Status");
            System.out.println("6. View Customer Transaction History");
            System.out.println("7. System Statistics");
            System.out.println("8. Logout");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    bankingSystem.viewAllCustomers();
                    break;
                case 2:
                    bankingSystem.viewAllTransactions();
                    break;
                case 3:
                    adminCreateAccount();
                    break;
                case 4:
                    adminDeleteAccount();
                    break;
                case 5:
                    adminToggleStatus();
                    break;
                case 6:
                    adminViewTransactionHistory();
                    break;
                case 7:
                    showSystemStatistics();
                    break;
                case 8:
                    System.out.println("Admin logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void adminCreateAccount() {
        System.out.println("\n=== CREATE CUSTOMER ACCOUNT (ADMIN) ===");
        System.out.println("(Type 'back' at any prompt to return to admin menu)");

        String name = getStringInputWithBack("Enter customer name: ");
        if (name == null) return;

        String pin = null;
        while (pin == null) {
            String inputPin = getStringInputWithBack("Enter 4-digit PIN for customer: ");
            if (inputPin == null) return;

            if (inputPin.matches("\\d{4}")) {
                pin = inputPin;
            } else {
                System.out.println("Invalid PIN! Please enter exactly 4 digits.");
            }
        }

        String accountType = null;
        while (accountType == null) {
            String inputType = getStringInputWithBack("Enter account type (SAVINGS/CHECKING): ");
            if (inputType == null) return;

            inputType = inputType.toUpperCase();
            if (inputType.equals("SAVINGS") || inputType.equals("CHECKING")) {
                accountType = inputType;
            } else {
                System.out.println("Invalid account type! Please enter SAVINGS or CHECKING.");
            }
        }

        Double initialDeposit = getDoubleInputWithBack("Enter initial deposit amount: $");
        if (initialDeposit == null) return;

        String accountNumber = bankingSystem.createCustomer(name, pin, accountType, initialDeposit);

        if (accountNumber != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("ACCOUNT CREATED SUCCESSFULLY!");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Customer Name: " + name);
            System.out.println("PIN: " + pin);
            System.out.println("Account Type: " + accountType);
            System.out.println("Initial Balance: $" + String.format("%.2f", initialDeposit));
            System.out.println("=".repeat(50));
        }
    }

    private static void adminDeleteAccount() {
        System.out.println("\n=== DELETE CUSTOMER ACCOUNT ===");
        System.out.println("(Type 'back' to return to admin menu)");

        String accountNumber = getStringInputWithBack("Enter account number to delete: ");
        if (accountNumber == null) return;

        String confirmation = getStringInputWithBack("Are you sure you want to delete this account? (yes/no): ");
        if (confirmation == null) return;

        if (confirmation.equalsIgnoreCase("yes")) {
            if (bankingSystem.deleteCustomer(accountNumber)) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("Account not found!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private static void adminToggleStatus() {
        System.out.println("\n=== TOGGLE ACCOUNT STATUS ===");
        System.out.println("(Type 'back' to return to admin menu)");

        String accountNumber = getStringInputWithBack("Enter account number: ");
        if (accountNumber == null) return;

        if (bankingSystem.toggleAccountStatus(accountNumber)) {
            System.out.println("Account status toggled successfully!");
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void adminViewTransactionHistory() {
        System.out.println("\n=== VIEW CUSTOMER TRANSACTION HISTORY ===");
        System.out.println("(Type 'back' to return to admin menu)");

        String accountNumber = getStringInputWithBack("Enter account number: ");
        if (accountNumber == null) return;

        bankingSystem.viewTransactionHistory(accountNumber);
    }

    private static void showSystemStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        // This would require additional methods in BankingSystem
        System.out.println("System statistics feature coming soon!");
        System.out.println("Current available features:");
        System.out.println("- View all customers");
        System.out.println("- View all transactions");
        System.out.println("- Account management");
    }

    private static void customerInterface() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("        CUSTOMER INTERFACE");
            System.out.println("=".repeat(40));
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Existing Account");
            System.out.println("3. Back to Main Menu");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    customerCreateAccount();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void customerCreateAccount() {
        System.out.println("\n=== CREATE NEW ACCOUNT ===");
        System.out.println("(Type 'back' at any prompt to return to customer interface)");

        String name = getStringInputWithBack("Enter your full name: ");
        if (name == null) return;

        String pin = null;
        while (pin == null) {
            String inputPin = getStringInputWithBack("Create a 4-digit PIN: ");
            if (inputPin == null) return;

            if (inputPin.matches("\\d{4}")) {
                String confirmPin = getStringInputWithBack("Confirm your PIN: ");
                if (confirmPin == null) return;

                if (inputPin.equals(confirmPin)) {
                    pin = inputPin;
                } else {
                    System.out.println("PINs do not match! Please try again.");
                }
            } else {
                System.out.println("Invalid PIN! Please enter exactly 4 digits.");
            }
        }

        String accountType = null;
        while (accountType == null) {
            System.out.println("\nAccount Types:");
            System.out.println("1. SAVINGS - Standard savings account");
            System.out.println("2. CHECKING - Current account for daily transactions");
            System.out.println("(Type 'back' to return to customer interface)");

            String typeInput = getStringInputWithBack("Choose account type (1-2): ");
            if (typeInput == null) return;

            try {
                int typeChoice = Integer.parseInt(typeInput);
                if (typeChoice == 1) {
                    accountType = "SAVINGS";
                } else if (typeChoice == 2) {
                    accountType = "CHECKING";
                } else {
                    System.out.println("Invalid choice! Please select 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
        }

        Double initialDeposit = null;
        while (initialDeposit == null) {
            Double inputDeposit = getDoubleInputWithBack("Enter initial deposit amount (minimum $50): $");
            if (inputDeposit == null) return;

            if (inputDeposit >= 50) {
                initialDeposit = inputDeposit;
            } else {
                System.out.println("Minimum initial deposit is $50.00");
            }
        }

        String accountNumber = bankingSystem.createCustomer(name, pin, accountType, initialDeposit);

        if (accountNumber != null) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🎉 CONGRATULATIONS! ACCOUNT CREATED SUCCESSFULLY! 🎉");
            System.out.println("=".repeat(60));
            System.out.println("IMPORTANT: Please save these details securely:");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Account Holder: " + name);
            System.out.println("Account Type: " + accountType);
            System.out.println("Initial Balance: $" + String.format("%.2f", initialDeposit));
            System.out.println("\n⚠️  SECURITY REMINDER:");
            System.out.println("- Never share your PIN with anyone");
            System.out.println("- Keep your account number confidential");
            System.out.println("- Contact admin if you suspect unauthorized access");
            System.out.println("=".repeat(60));
        }
    }

    private static void customerLogin() {
        System.out.println("\n=== CUSTOMER LOGIN ===");
        System.out.println("(Type 'back' at any prompt to return to customer interface)");

        String accountNumber = getStringInputWithBack("Enter your account number: ");
        if (accountNumber == null) return;

        String pin = getStringInputWithBack("Enter your PIN: ");
        if (pin == null) return;

        Customer customer = bankingSystem.customerLogin(accountNumber, pin);

        if (customer != null) {
            customerMenu(accountNumber, pin, customer);
        }
    }

    private static void customerMenu(String accountNumber, String pin, Customer customer) {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Welcome, " + customer.getName() + "!");
            System.out.println("Account: " + accountNumber);
            System.out.println("=".repeat(50));
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Recent Transactions (Last 5)");
            System.out.println("6. View All Transaction History");
            System.out.println("7. Account Information");
            System.out.println("8. Logout");
            System.out.println("=".repeat(50));

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    bankingSystem.checkBalance(accountNumber, pin);
                    break;
                case 2:
                    customerDeposit(accountNumber, pin);
                    break;
                case 3:
                    customerWithdraw(accountNumber, pin);
                    break;
                case 4:
                    customerTransfer(accountNumber, pin);
                    break;
                case 5:
                    bankingSystem.viewRecentTransactions(accountNumber, pin, 5);
                    break;
                case 6:
                    bankingSystem.viewTransactionHistory(accountNumber);
                    break;
                case 7:
                    showAccountInfo(customer);
                    break;
                case 8:
                    System.out.println("Thank you for using CvSU Banking System!");
                    System.out.println("Logged out successfully. Stay safe!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void customerDeposit(String accountNumber, String pin) {
        System.out.println("\n=== DEPOSIT MONEY ===");
        System.out.println("(Type 'back' to return to customer menu)");

        Double amount = getDoubleInputWithBack("Enter deposit amount: $");
        if (amount == null) return;

        if (amount <= 0) {
            System.out.println("Invalid amount! Deposit amount must be positive.");
            return;
        }

        bankingSystem.deposit(accountNumber, pin, amount);
    }

    private static void customerWithdraw(String accountNumber, String pin) {
        System.out.println("\n=== WITHDRAW MONEY ===");
        System.out.println("(Type 'back' to return to customer menu)");

        // First show current balance
        System.out.println("Checking your current balance...");
        bankingSystem.checkBalance(accountNumber, pin);

        Double amount = getDoubleInputWithBack("\nEnter withdrawal amount: $");
        if (amount == null) return;

        if (amount <= 0) {
            System.out.println("Invalid amount! Withdrawal amount must be positive.");
            return;
        }

        bankingSystem.withdraw(accountNumber, pin, amount);
    }

    private static void customerTransfer(String accountNumber, String pin) {
        System.out.println("\n=== TRANSFER MONEY ===");
        System.out.println("(Type 'back' at any prompt to return to customer menu)");

        // Show current balance first
        System.out.println("Your current balance:");
        bankingSystem.checkBalance(accountNumber, pin);

        String toAccount = getStringInputWithBack("\nEnter destination account number: ");
        if (toAccount == null) return;

        if (toAccount.equals(accountNumber)) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }

        Double amount = getDoubleInputWithBack("Enter transfer amount: $");
        if (amount == null) return;

        if (amount <= 0) {
            System.out.println("Invalid amount! Transfer amount must be positive.");
            return;
        }

        String confirmation = getStringInputWithBack("Confirm transfer of $" + String.format("%.2f", amount) +
                " to account " + toAccount + "? (yes/no): ");
        if (confirmation == null) return;

        if (confirmation.equalsIgnoreCase("yes")) {
            bankingSystem.transfer(accountNumber, pin, toAccount, amount);
        } else {
            System.out.println("Transfer cancelled.");
        }
    }

    private static void showAccountInfo(Customer customer) {
        System.out.println("\n=== ACCOUNT INFORMATION ===");
        Account account = customer.getAccount();
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Holder: " + customer.getName());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println("Account Status: " + (account.isActive() ? "Active" : "Suspended"));
        System.out.println("=".repeat(40));
    }

    // New utility methods for input handling with back option
    private static String getStringInputWithBack(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) {
            System.out.println("Returning to previous menu...");
            return null;
        }
        return input;
    }

    private static Double getDoubleInputWithBack(String prompt) {
        while (true) {
            String input = getStringInputWithBack(prompt);
            if (input == null) return null; // User chose to go back

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount or 'back' to return.");
            }
        }
    }

    // Original utility methods for input handling (still used in menus)
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount.");
            }
        }
    }
}
