# CvSU Banking System

A comprehensive console-based banking system developed in Java. This system provides both administrative and customer interfaces for managing bank accounts and transactions.

## Features

### Admin Features
- **Admin Authentication**: Secure admin login with password protection
- **Customer Management**: Create, view, and delete customer accounts
- **Account Status Control**: Activate or suspend customer accounts
- **Transaction Monitoring**: View all transactions across the system
- **Customer Transaction History**: View individual customer transaction records
- **System Statistics**: Overview of banking operations

### Customer Features
- **Account Creation**: Self-service account registration
- **Secure Login**: PIN-based authentication system
- **Balance Inquiry**: Check current account balance
- **Deposit Operations**: Add money to accounts
- **Withdrawal Operations**: Withdraw money with balance validation
- **Money Transfer**: Transfer funds between accounts
- **Transaction History**: View recent and complete transaction records
- **Account Information**: View account details and status

### Account Types
- **Savings Account**: Standard savings account for long-term deposits
- **Checking Account**: Current account for daily transactions

## System Architecture

### Core Classes

- **Main.java**: Entry point and user interface management
- **BankingSystem.java**: Core business logic and system operations
- **Customer.java**: Customer entity with PIN validation
- **Account.java**: Account entity with balance operations
- **Transaction.java**: Transaction records with timestamps
- **TransactionService.java**: Transaction management and filtering
- **AccountService.java**: Account validation utilities
- **FileManager.java**: Data persistence and file operations

## Technical Specifications

### Requirements
- Java 8 or higher
- Console/Terminal environment
- File system access for data persistence

### Data Storage
- **customers.txt**: Customer and account information
- **transactions.txt**: Transaction history records
- Automatic backup functionality available

### Security Features
- 4-digit PIN authentication for customers
- Admin password protection
- Account suspension capabilities
- Transaction validation and verification

## Installation & Setup

1. **Clone or Download** the project files
2. **Compile** all Java files:
   ```bash
   javac *.java
   ```
3. **Run** the application:
   ```bash
   java Main
   ```

## Usage Guide

### Starting the System
When you run the application, you'll see the main menu:
```
==================================================
    WELCOME TO ************
           BANKING SYSTEM
==================================================
1. Admin Login
2. Customer Interface
3. Exit
```

### Admin Access
- Default admin password: `admin123`
- Admin can manage all customer accounts and view system-wide data

### Customer Registration
1. Select "Customer Interface" from main menu
2. Choose "Create New Account"
3. Provide required information:
   - Full name
   - 4-digit PIN (must be exactly 4 digits)
   - Account type (Savings or Checking)
   - Initial deposit (minimum $50)

### Customer Login
1. Enter your account number (format: ACC + timestamp)
2. Enter your 4-digit PIN
3. Access your account dashboard

## File Structure

```
banking-system/
├── Main.java              # Main application entry point
├── BankingSystem.java     # Core banking operations
├── Customer.java          # Customer entity
├── Account.java           # Account entity
├── Transaction.java       # Transaction entity
├── TransactionService.java # Transaction management
├── AccountService.java    # Account utilities
├── FileManager.java       # Data persistence
├── customers.txt          # Customer data (auto-generated)
├── transactions.txt       # Transaction data (auto-generated)
└── README.md             # This file
```

## Key Features Explained

### Account Number Generation
- Format: `ACC` + timestamp
- Ensures unique account numbers
- Example: `ACC1640995200000`

### PIN Security
- Must be exactly 4 digits
- Stored as plain text (for educational purposes)
- Validated on every transaction

### Transaction Types
- **DEPOSIT**: Cash deposits
- **WITHDRAWAL**: Cash withdrawals
- **TRANSFER_OUT**: Outgoing transfers
- **TRANSFER_IN**: Incoming transfers

### Data Persistence
- Automatic saving after each operation
- Pipe-delimited format for easy parsing
- Backup functionality available

## Error Handling

The system includes comprehensive error handling for:
- Invalid PIN formats
- Insufficient funds
- Non-existent accounts
- Suspended accounts
- Invalid transaction amounts
- File I/O operations

## Sample Operations

### Creating an Account
```
Enter your full name: John Doe
Create a 4-digit PIN: 1234
Choose account type: 1 (Savings)
Enter initial deposit: 100.00
```

### Making a Transfer
```
Enter destination account number: ACC1640995300000
Enter transfer amount: 50.00
Current balance: $100.00
New balance: $50.00
```

## Limitations

- Console-based interface only
- Single-user session at a time
- Basic security implementation
- No network connectivity
- File-based storage only

## Future Enhancements

- GUI interface
- Database integration
- Multi-user support
- Enhanced security with encryption
- Online banking features
- Card/ATM integration
- Interest calculation
- Account statements
- Email notifications

## Troubleshooting

### Common Issues

1. **"Invalid PIN" Error**
   - Ensure PIN is exactly 4 digits
   - No letters or special characters allowed

2. **"Account Suspended" Message**
   - Contact admin to reactivate account
   - Admin can toggle account status

3. **File Not Found Errors**
   - System will create data files automatically
   - Ensure write permissions in directory

4. **Transaction Failures**
   - Check account balance before withdrawals
   - Verify destination account exists for transfers

## Developer Notes

### Code Structure
- Object-oriented design principles
- Separation of concerns
- Modular architecture
- Clean code practices

### Testing
- Manual testing through console interface
- Test various scenarios (valid/invalid inputs)
- Verify data persistence
- Check transaction integrity

## License

This project is developed for educational purposes.

## Support

For technical support or questions about the banking system, please contact the development team or refer to the system documentation.

---


*Secure • Reliable • User-Friendly*
