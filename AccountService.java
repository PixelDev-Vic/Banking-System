import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AccountService {
    private Map<String, Account> accounts;

    public AccountService() {
        this.accounts = new HashMap<>();
    }

    public Account createAccount(String accountNumber, String customerName, String accountType, double initialBalance) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists");
        }

        Account account = new Account(accountNumber, customerName, accountType, initialBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public boolean deleteAccount(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            accounts.remove(accountNumber);
            return true;
        }
        return false;
    }

    public boolean suspendAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setActive(false);
            return true;
        }
        return false;
    }

    public boolean activateAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setActive(true);
            return true;
        }
        return false;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.isActive() && amount > 0) {
            account.deposit(amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.isActive() && amount > 0) {
            return account.withdraw(amount);
        }
        return false;
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accounts.get(fromAccountNumber);
        Account toAccount = accounts.get(toAccountNumber);

        if (fromAccount != null && toAccount != null &&
                fromAccount.isActive() && toAccount.isActive() &&
                amount > 0) {

            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                return true;
            }
        }
        return false;
    }

    public double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getBalance() : -1;
    }

    public List<Account> getAccountsByType(String accountType) {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getAccountType().equalsIgnoreCase(accountType)) {
                result.add(account);
            }
        }
        return result;
    }

    public List<Account> getActiveAccounts() {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.isActive()) {
                result.add(account);
            }
        }
        return result;
    }

    public List<Account> getSuspendedAccounts() {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (!account.isActive()) {
                result.add(account);
            }
        }
        return result;
    }

    public void calculateInterestForAllSavingsAccounts() {
        for (Account account : accounts.values()) {
            if ("SAVINGS".equals(account.getAccountType())) {
                account.calculateInterest();
            }
        }
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }
}