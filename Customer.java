public class Customer {
    private String name;
    private String pin;
    private Account account;

    public Customer(String name, String pin, Account account) {
        this.name = name;
        this.pin = pin;
        this.account = account;
    }

    // Validate PIN (4 digits)
    public boolean validatePin(String inputPin) {
        return this.pin != null && this.pin.equals(inputPin);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        // Validate PIN format before setting
        if (pin != null && pin.matches("\\d{4}")) {
            this.pin = pin;
        } else {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", account=" + account +
                '}';
    }
}
