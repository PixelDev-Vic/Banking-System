import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Customer {
    private String name;
    private String passwordHash;
    private Account account;
    private int failedLoginAttempts;
    private boolean accountLocked;

    public Customer(String name, String password, Account account) {
        this.name = name;
        this.passwordHash = hashPassword(password);
        this.account = account;
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
    }

    // Constructor for loading from file
    public Customer(String name, String passwordHash, Account account, int failedAttempts, boolean locked) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.account = account;
        this.failedLoginAttempts = failedAttempts;
        this.accountLocked = locked;
    }

    public boolean validatePassword(String password) {
        if (accountLocked) {
            return false;
        }

        if (hashPassword(password).equals(passwordHash)) {
            failedLoginAttempts = 0;
            return true;
        } else {
            failedLoginAttempts++;
            if (failedLoginAttempts >= 3) {
                accountLocked = true;
                System.out.println("Account locked due to multiple failed login attempts.");
            }
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public void unlockAccount() {
        this.accountLocked = false;
        this.failedLoginAttempts = 0;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Override
    public String toString() {
        return String.format("Customer{name='%s', account=%s, locked=%s}",
                name, account.getAccountNumber(), accountLocked);
    }
}