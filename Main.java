import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankingSystem bankingSystem = new BankingSystem();

    public static void main(String[] args) {
        System.out.println("=== Welcome to Java Banking System ===");

        while (true) {
            showMainMenu();
            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    System.out.println("Thank you for using Java Banking System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Customer Menu");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void adminLogin() {
        while (true) {
            System.out.println("\n=== ADMIN LOGIN ===");
            System.out.println("1. Enter Password");
            System.out.println("2. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter admin password: ");
                    String password = scanner.nextLine();

                    if (bankingSystem.adminLogin(password)) {
                        adminMenu();
                        return;
                    } else {
                        System.out.println("Invalid admin password!");
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Create Customer Account");
            System.out.println("2. View All Customers");
            System.out.println("3. View All Transactions");
            System.out.println("4. Delete Customer Account");
            System.out.println("5. Suspend/Activate Account");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    createCustomerAccount();
                    break;
                case 2:
                    bankingSystem.viewAllCustomers();
                    break;
                case 3:
                    bankingSystem.viewAllTransactions();
                    break;
                case 4:
                    deleteCustomerAccount();
                    break;
                case 5:
                    toggleAccountStatus();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createCustomerAccount() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Cancel");
        System.out.print("Choice: ");
        int typeChoice = getValidIntInput();

        if (typeChoice == 3) {
            System.out.println("Account creation cancelled.");
            return;
        }

        String accountType = (typeChoice == 1) ? "SAVINGS" : "CURRENT";

        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = getValidDoubleInput();

        if (initialDeposit < 0) {
            System.out.println("Initial deposit cannot be negative!");
            return;
        }

        String accountNumber = bankingSystem.createCustomer(name, password, accountType, initialDeposit);
        if (accountNumber != null) {
            System.out.println("Account created successfully! Account Number: " + accountNumber);
        } else {
            System.out.println("Failed to create account!");
        }
    }

    private static void deleteCustomerAccount() {
        System.out.print("Enter account number to delete: ");
        String accountNumber = scanner.nextLine();

        System.out.println("Are you sure you want to delete this account?");
        System.out.println("1. Yes, delete account");
        System.out.println("2. No, cancel");
        System.out.print("Choice: ");
        int confirmation = getValidIntInput();

        if (confirmation != 1) {
            System.out.println("Account deletion cancelled.");
            return;
        }

        if (bankingSystem.deleteCustomer(accountNumber)) {
            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Account not found or deletion failed!");
        }
    }

    private static void toggleAccountStatus() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (bankingSystem.toggleAccountStatus(accountNumber)) {
            System.out.println("Account status updated successfully!");
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void customerMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Register New Account");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    customerRegistration();
                    break;
                case 2:
                    customerLoginMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerRegistration() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account (Minimum deposit: $100)");
        System.out.println("2. Current Account (Minimum deposit: $500)");
        System.out.println("3. Cancel Registration");
        System.out.print("Choice: ");
        int typeChoice = getValidIntInput();

        if (typeChoice == 3) {
            System.out.println("Registration cancelled.");
            return;
        }

        String accountType = (typeChoice == 1) ? "SAVINGS" : "CURRENT";
        double minDeposit = (typeChoice == 1) ? 100.0 : 500.0;

        System.out.print("Enter initial deposit amount ($" + minDeposit + " minimum): ");
        double initialDeposit = getValidDoubleInput();

        if (initialDeposit < minDeposit) {
            System.out.println("Initial deposit must be at least $" + minDeposit);
            return;
        }

        String accountNumber = bankingSystem.createCustomer(name, password, accountType, initialDeposit);
        if (accountNumber != null) {
            System.out.println("Registration successful! Your Account Number: " + accountNumber);
            System.out.println("Please remember your account number for future logins.");
        } else {
            System.out.println("Registration failed!");
        }
    }

    private static void customerLoginMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER LOGIN ===");
            System.out.println("1. Login to Account");
            System.out.println("2. Back to Customer Menu");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    performCustomerLogin();
                    return; // Return after login attempt (success or failure)
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void performCustomerLogin() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = bankingSystem.customerLogin(accountNumber, password);
        if (customer != null) {
            customerDashboard(customer);
        } else {
            System.out.println("Invalid account number or password!");
        }
    }

    private static void customerDashboard(Customer customer) {
        while (true) {
            System.out.println("\n=== CUSTOMER DASHBOARD ===");
            System.out.println("Welcome, " + customer.getName() + "!");
            System.out.println("Account: " + customer.getAccount().getAccountNumber());
            System.out.println("Balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            System.out.println();
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. View Account Details");
            System.out.println("5. View Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    depositMoneyMenu(customer);
                    break;
                case 2:
                    withdrawMoneyMenu(customer);
                    break;
                case 3:
                    transferMoneyMenu(customer);
                    break;
                case 4:
                    viewAccountDetails(customer);
                    break;
                case 5:
                    viewTransactionHistory(customer);
                    break;
                case 6:
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void depositMoneyMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== DEPOSIT MONEY ===");
            System.out.println("Current Balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            System.out.println("1. Make Deposit");
            System.out.println("2. Back to Dashboard");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: $");
                    double amount = getValidDoubleInput();

                    if (amount <= 0) {
                        System.out.println("Deposit amount must be positive!");
                        break;
                    }

                    if (bankingSystem.deposit(customer.getAccount().getAccountNumber(), amount)) {
                        System.out.println("Deposit successful! New balance: $" +
                                String.format("%.2f", customer.getAccount().getBalance()));
                        return; // Return to dashboard after successful deposit
                    } else {
                        System.out.println("Deposit failed!");
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void withdrawMoneyMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== WITHDRAW MONEY ===");
            System.out.println("Current Balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            System.out.println("1. Make Withdrawal");
            System.out.println("2. Back to Dashboard");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter withdrawal amount: $");
                    double amount = getValidDoubleInput();

                    if (amount <= 0) {
                        System.out.println("Withdrawal amount must be positive!");
                        break;
                    }

                    if (bankingSystem.withdraw(customer.getAccount().getAccountNumber(), amount)) {
                        System.out.println("Withdrawal successful! New balance: $" +
                                String.format("%.2f", customer.getAccount().getBalance()));
                        return; // Return to dashboard after successful withdrawal
                    } else {
                        System.out.println("Withdrawal failed! Insufficient balance or account suspended.");
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void transferMoneyMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== TRANSFER MONEY ===");
            System.out.println("Current Balance: $" + String.format("%.2f", customer.getAccount().getBalance()));
            System.out.println("1. Make Transfer");
            System.out.println("2. Back to Dashboard");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter recipient account number: ");
                    String toAccount = scanner.nextLine();

                    System.out.print("Enter transfer amount: $");
                    double amount = getValidDoubleInput();

                    if (amount <= 0) {
                        System.out.println("Transfer amount must be positive!");
                        break;
                    }

                    if (bankingSystem.transfer(customer.getAccount().getAccountNumber(), toAccount, amount)) {
                        System.out.println("Transfer successful! New balance: $" +
                                String.format("%.2f", customer.getAccount().getBalance()));
                        return; // Return to dashboard after successful transfer
                    } else {
                        System.out.println("Transfer failed! Check recipient account or insufficient balance.");
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAccountDetails(Customer customer) {
        Account account = customer.getAccount();
        System.out.println("\n=== ACCOUNT DETAILS ===");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Holder: " + customer.getName());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println("Status: " + (account.isActive() ? "Active" : "Suspended"));
        System.out.println("Created: " + account.getCreatedDate());

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void viewTransactionHistory(Customer customer) {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        bankingSystem.viewTransactionHistory(customer.getAccount().getAccountNumber());

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static int getValidIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.print("Please enter a valid number: ");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    private static double getValidDoubleInput() {
        while (true) {
            try {
                double input = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.print("Please enter a valid amount: ");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
}