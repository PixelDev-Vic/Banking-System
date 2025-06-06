# Java Banking System

A comprehensive banking system implemented in Java with full functionality for account management, transactions, and data persistence.

🏗️ Project Structure


```
BankingSystem/
├── Main.java                 # Entry point and user interface
├── BankingSystem.java        # Core banking operations
├── Customer.java             # Customer model with authentication
├── Account.java              # Account model with balance management
├── Transaction.java          # Transaction model
├── AccountService.java       # Account management service
├── TransactionService.java   # Transaction management service
├── FileManager.java          # File-based data persistence
└── data/                     # Data storage directory
    ├── customers.txt         # Customer and account data
    └── transactions.txt      # Transaction history
🚀 Features
```

Admin Features
- **Secure Login**: Admin authentication with password protection
- **Customer Management**: Create, view, delete, and suspend customer accounts
- **Account Oversight**: View all accounts and their details
- **Transaction Monitoring**: View complete transaction history across all accounts
- **Account Status Control**: Suspend or activate customer accounts

Customer Features
- **Account Registration**: Self-registration with account type selection
- **Secure Login**: Password-protected authentication with account lockout protection
- **Banking Operations**:
  - Deposit money
  - Withdraw money (with minimum balance checks)
  - Transfer funds between accounts
- **Account Management**:
  - View account details and balance
  - View complete transaction history
- **Security**: Account lockout after 3 failed login attempts

Account Types
1. **Savings Account**
   - Minimum balance: $50
   - Interest rate: 3% annually
   - Minimum initial deposit: $100
   - Automatic interest calculation

2. **Current Account**
   - Minimum balance: $100
   - Interest rate: 1% annually
   - Minimum initial deposit: $500
   - Business-oriented features

🛠️ Technical Features

Security
- **Password Hashing**: SHA-256 encryption for all passwords
- **Account Lockout**: Automatic lockout after 3 failed login attempts
- **Input Validation**: Comprehensive validation for all user inputs
- **Session Management**: Secure login/logout functionality

Data Management
- **File Persistence**: Data stored in text files for persistence across sessions
- **Backup System**: Automatic data backup functionality
- **Data Integrity**: Error handling and data validation
- **CSV Export**: Export account data to CSV format

Transaction Management
- **Unique Transaction IDs**: Auto-generated unique identifiers
- **Transaction History**: Complete audit trail of all operations
- **Transaction Types**: Deposit, Withdrawal, Transfer In/Out
- **Balance Tracking**: Real-time balance updates

📋 How to Run

Prerequisites
- Java 8 or higher
- Command line access

Compilation
```bash
javac *.java
```

Execution
```bash
java Main
```

🎯 Usage Guide

Admin Access
1. Select "Admin Login" from the main menu
2. Enter admin password: `admin123`
3. Use admin features to manage the banking system

Customer Registration
1. Select "Customer Menu" → "Register New Account"
2. Provide personal information and choose account type
3. Make initial deposit (minimum $100 for Savings, $500 for Current)
4. Remember your account number for future logins

Customer Login
1. Select "Customer Menu" → "Login"
2. Enter your account number and password
3. Access your personal banking dashboard

Banking Operations
- **Deposit**: Add money to your account
- **Withdraw**: Remove money (subject to minimum balance)
- **Transfer**: Send money to another account
- **View Details**: Check account information and balance
- **Transaction History**: Review all past transactions

🔧 Configuration

Admin Password
Default admin password is `admin123`. To change it, modify the `ADMIN_PASSWORD` constant in `BankingSystem.java`.

Interest Rates
- Savings accounts: 3% annual (modifiable in `Account.java`)
- Current accounts: 1% annual (modifiable in `Account.java`)

Minimum Balances
- Savings accounts: $50 minimum
- Current accounts: $100 minimum

File Locations
- Customer data: `data/customers.txt`
- Transaction data: `data/transactions.txt`
- Backups: `data/backup_[timestamp]/`

📊 Data Format

Customer Data Format

```
Name|PasswordHash|AccountNumber|AccountType|Balance|Active|CreatedDate|InterestRate|LastInterestCalc|FailedAttempts|Locked
```

Transaction Data Format

```
TransactionID|AccountNumber|Type|Amount|Timestamp|Description|BalanceAfter
```

🛡️ Security Considerations

1. **Password Security**: All passwords are hashed using SHA-256
2. **Account Lockout**: Prevents brute force attacks
3. **Input Validation**: Prevents invalid data entry
4. **Session Management**: Secure login/logout process
5. **Data Integrity**: Error handling and validation

🚨 Error Handling

The system includes comprehensive error handling for:
- Invalid input data
- File I/O operations
- Mathematical operations
- Authentication failures
- Account status issues

📈 Future Enhancements

Potential improvements that could be added:
- GUI interface using JavaFX or Swing
- Database integration (MySQL, PostgreSQL)
- Network capabilities for remote access
- Advanced reporting and analytics
- Mobile app integration
- Email notifications
- Loan management system
- Credit/debit card integration

🐛 Troubleshooting

Common Issues

1. **Data directory not found**
   - Solution: The system automatically creates the data directory

2. **Account locked**
   - Solution: Contact admin to unlock the account

3. **Insufficient balance**
   - Solution: Ensure account has sufficient funds above minimum balance

4. **File permission errors**
   - Solution: Ensure write permissions for the data directory

📝 Testing

Test Scenarios

1. **Admin Functions**
   - Login with correct/incorrect password
   - Create various account types
   - View all customers and transactions
   - Suspend/activate accounts

2. **Customer Functions**
   - Register new accounts
   - Login with valid/invalid credentials
   - Perform banking operations
   - View account details and history

3. **Edge Cases**
   - Minimum balance violations
   - Transfer to non-existent accounts
   - Negative amounts
   - Account lockout scenarios

📄 License

This project is created for educational purposes. Feel free to use and modify as needed.

---

**Note**: This banking system is designed for educational and demonstration purposes. For production use, additional security measures and professional auditing would be required.
