import java.util.*;
import java.util.stream.Collectors;

// AccountService class
class AccountService {

    public boolean validateAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.startsWith("ACC") && accountNumber.length() > 3;
    }

    public boolean validateAccountType(String accountType) {
        return accountType != null &&
                (accountType.equalsIgnoreCase("SAVINGS") || accountType.equalsIgnoreCase("CHECKING"));
    }

    public boolean validateAmount(double amount) {
        return amount > 0;
    }

    public String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }
}

