import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private String accountNumber;
    private String customerName;
    private String accountType; // SAVINGS or CURRENT
    private double balance;
    private boolean isActive;
    private LocalDateTime createdDate;
    private double interestRate;
    private LocalDateTime lastInterestCalculation;

    public Account(String accountNumber, String customerName, String accountType, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.accountType = accountType.toUpperCase();
        this.balance = initialBalance;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
        this.lastInterestCalculation = LocalDateTime.now();

        // Set interest rate based on account type
        if ("SAVINGS".equals(this.accountType)) {
            this.interestRate = 0.03; // 3% annual interest for savings
        } else {
            this.interestRate = 0.01; // 1% annual interest for current accounts
        }
    }

    // Constructor for loading from file
    public Account(String accountNumber, String customerName, String accountType,
                   double balance, boolean isActive, LocalDateTime createdDate,
                   double interestRate, LocalDateTime lastInterestCalculation) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.interestRate = interestRate;
        this.lastInterestCalculation = lastInterestCalculation;
    }

    public synchronized void deposit(double amount) {
        if (amount > 0 && isActive) {
            balance += amount;
            calculateInterest();
        }
    }

    public synchronized boolean withdraw(double amount) {
        if (!isActive) {
            return false;
        }

        if (amount > 0 && balance >= amount) {
            // Check minimum balance requirements
            double minBalance = getMinimumBalance();
            if (balance - amount >= minBalance) {
                balance -= amount;
                calculateInterest();
                return true;
            } else {
                System.out.println("Insufficient balance. Minimum balance required: $" + minBalance);
                return false;
            }
        }
        return false;
    }

    private double getMinimumBalance() {
        // Savings accounts require $50 minimum, Current accounts require $100 minimum
        return "SAVINGS".equals(accountType) ? 50.0 : 100.0;
    }

    public void calculateInterest() {
        if ("SAVINGS".equals(accountType)) {
            LocalDateTime now = LocalDateTime.now();
            long monthsSinceLastCalculation = java.time.temporal.ChronoUnit.MONTHS.between(
                    lastInterestCalculation, now);

            if (monthsSinceLastCalculation >= 1) {
                double monthlyInterest = balance * (interestRate / 12) * monthsSinceLastCalculation;
                balance += monthlyInterest;
                lastInterestCalculation = now;

                if (monthlyInterest > 0) {
                    System.out.println("Interest added: $" + String.format("%.2f", monthlyInterest));
                }
            }
        }
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType.toUpperCase();
    }

    public double getBalance() {
        calculateInterest(); // Always calculate latest interest before returning balance
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDateTime getLastInterestCalculation() {
        return lastInterestCalculation;
    }

    public void setLastInterestCalculation(LocalDateTime lastInterestCalculation) {
        this.lastInterestCalculation = lastInterestCalculation;
    }

    @Override
    public String toString() {
        return String.format("Account{number='%s', type='%s', balance=%.2f, active=%s}",
                accountNumber, accountType, balance, isActive);
    }
}