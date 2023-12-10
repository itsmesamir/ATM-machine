package main.java.ui;

import java.util.List;
import java.util.Scanner;
import main.java.model.Customer;
import main.java.service.AuthenticationService;
import main.java.service.FileService;
import main.java.service.ReceiptService;
import main.java.service.TransactionService;

public class ConsoleUI {

  private AuthenticationService authService;
  private FileService fileService;
  private ReceiptService receiptService;
  private TransactionService transactionService;

  private Customer loggedInCustomer;

  public ConsoleUI() {
    // Initialize your services here if needed
    this.authService = new AuthenticationService();
    this.fileService = new FileService();
    this.receiptService = new ReceiptService("Bank of USA", "texas");
    this.transactionService =
      new TransactionService(fileService, receiptService, authService);
  }

  public ConsoleUI(
    AuthenticationService authService,
    FileService fileService,
    ReceiptService receiptService,
    TransactionService transactionService
  ) {
    this.authService = authService;
    this.fileService = fileService;
    this.receiptService = receiptService;
    this.transactionService = transactionService;
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      try {
        displayMainMenu();
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
          case 1:
            login();
            break;
          case 2:
            displayAccountBalances();
            break;
          case 3:
            if (isLoggedIn()) {
              deposit();
            } else {
              System.out.println("Please login first.");
            }
            break;
          case 4:
            if (isLoggedIn()) {
              withdraw();
            } else {
              System.out.println("Please login first.");
            }
            break;
          case 5:
            if (isLoggedIn()) {
              transfer();
            } else {
              System.out.println("Please login first.");
            }
            break;
          case 0:
            System.out.println("Exiting. Goodbye!");
            System.exit(0);
            break;
          default:
            System.out.println("Invalid choice. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("An unexpected error occurred. Please try again.");
        // Clear the scanner's buffer to prevent an infinite loop
        scanner.nextLine();
      }
    }
  }

  private void displayMainMenu() {
    if (!isLoggedIn()) {
      System.out.println("1. Login");
    }
    System.out.println("2. Display Account Balances");
    System.out.println("3. Deposit");
    System.out.println("4. Withdraw");
    System.out.println("5. Transfer");
    System.out.println("0. Exit");
    System.out.print("Enter your choice: ");
  }

  private void login() {
    Scanner scanner = new Scanner(System.in);

    try {
      System.out.print("Enter Customer ID: ");
      int customerId = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character

      System.out.print("Enter PIN: ");
      int pin = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character

      System.out.println("Authenticating.....");
      List<String> customersData = fileService.readCustomersFromFile();

      boolean isAuthorized = authService.authenticateCustomer(
        customerId,
        pin,
        customersData
      );

      loggedInCustomer = authService.getCustomerById(customerId);

      if (isAuthorized && loggedInCustomer != null) {
        // Successful login, perform actions
        System.out.println(
          "Login successful! Welcome, " + loggedInCustomer.getName() + "!"
        );
      } else {
        System.out.println("Login failed. Invalid credentials.");
        loggedInCustomer = null; // Reset logged-in customer on failed login
      }
    } catch (Exception e) {
      System.out.println("An unexpected error occurred. Please try again.");
      // Clear the scanner's buffer to prevent an infinite loop
      scanner.nextLine();
    }
  }

  private void displayAccountBalances() {
    if (isLoggedIn()) {
      System.out.println(
        "Account balances for " + loggedInCustomer.getName() + ":"
      );
      List<String> accountsData = fileService.readAccountsFromFile();
      List<String> transactionsData = fileService.readTransactionsFromFile();

      double balance = 0.0;

      for (String accountData : accountsData) {
        String[] accountParts = accountData.split(",");
        int accountId = Integer.parseInt(accountParts[0]);
        int customerAccountId = Integer.parseInt(accountParts[1]);
        double accountBalance = Double.parseDouble(accountParts[5]);

        System.out.println("accountParts: " + accountBalance);

        if (customerAccountId == loggedInCustomer.getCustomerId()) {
          balance += accountBalance;
        }
      }

      System.out.println("Total Balance: $" + balance);
    } else {
      System.out.println("Please login first.");
    }
  }

  private void deposit() {
    Scanner scanner = new Scanner(System.in);

    // Get the logged-in customer's account
    List<String> accountsData = fileService.readAccountsFromFile();
    String customerAccount = getCustomerAccount(accountsData);

    if (customerAccount == null) {
      System.out.println("No account found for the logged-in customer.");
      return;
    }

    System.out.println("Customer Account: " + customerAccount);

    // Extract the account ID from the customer's account
    int toAccountId = extractAccountId(customerAccount);

    // Get the deposit amount
    System.out.print("Enter deposit amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine(); // Consume the newline character

    // Process the deposit
    transactionService.processDeposit(
      loggedInCustomer.getCustomerId(),
      toAccountId,
      amount,
      accountsData
    );

    // Log the transaction with both fromAccountId and toAccountId
    transactionService.logTransaction(
      generateTransactionId(),
      loggedInCustomer.getCustomerId(),
      toAccountId,
      amount
    );

    // Display updated balance
    System.out.println(
      "Deposit successful! Updated balance: $" +
      transactionService.calculateAccountBalance(
        loggedInCustomer.getCustomerId(),
        accountsData,
        fileService.readTransactionsFromFile()
      )
    );
  }

  private String getCustomerAccount(List<String> accountsData) {
    int loggedInCustomerId = loggedInCustomer.getCustomerId();

    for (String accountData : accountsData) {
      String[] accountParts = accountData.split(",");
      int customerAccountId = Integer.parseInt(accountParts[1]);

      if (customerAccountId == loggedInCustomerId) {
        return accountData;
      }
    }

    return null;
  }

  private String getAccountByAccountId(
    List<String> accountsData,
    int accountId
  ) {
    for (String accountData : accountsData) {
      String[] accountParts = accountData.split(",");
      int currentAccountId = Integer.parseInt(accountParts[0]);

      if (currentAccountId == accountId) {
        return accountData;
      }
    }

    return null;
  }

  private int extractAccountId(String accountData) {
    String[] accountParts = accountData.split(",");
    return Integer.parseInt(accountParts[0]); // Assuming account ID is the first part
  }

  private void withdraw() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter withdrawal amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine(); // Consume the newline character

    List<String> accountsData = fileService.readAccountsFromFile();
    double currentBalance = transactionService.calculateAccountBalance(
      loggedInCustomer.getCustomerId(),
      accountsData,
      fileService.readTransactionsFromFile()
    );

    String customerAccount = getCustomerAccount(accountsData);

    if (customerAccount == null) {
      System.out.println("No account found for the logged-in customer.");
      return;
    }

    System.out.println("Customer Account: " + customerAccount);

    // Extract the account ID from the customer's account
    int toAccountId = extractAccountId(customerAccount);

    if (currentBalance >= amount) {
      transactionService.processWithdrawal(
        loggedInCustomer.getCustomerId(),
        toAccountId,
        amount,
        accountsData
      );

      // Log the transaction
      transactionService.logTransaction(
        generateTransactionId(),
        toAccountId,
        toAccountId,
        amount
      );

      System.out.println(
        "Withdrawal successful! Updated balance: $" +
        transactionService.calculateAccountBalance(
          loggedInCustomer.getCustomerId(),
          accountsData,
          fileService.readTransactionsFromFile()
        )
      );
    } else {
      System.out.println("Insufficient funds for withdrawal.");
    }
  }

  private void transfer() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter recipient's account ID: ");
    int recipientId = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character

    System.out.print("Enter transfer amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine(); // Consume the newline character

    List<String> accountsData = fileService.readAccountsFromFile();
    String customerAccount = getCustomerAccount(accountsData);

    if (customerAccount == null) {
      System.out.println("No account found for the logged-in customer.");
      return;
    }

    System.out.println("Customer Account: " + customerAccount);

    // Extract the account ID from the customer's account
    int sourceAccount = extractAccountId(customerAccount);

    // Check if recipient account exists
    if (getAccountByAccountId(accountsData, recipientId) != null) {
      transactionService.processTransfer(
        loggedInCustomer.getCustomerId(),
        sourceAccount,
        recipientId,
        amount,
        accountsData
      );

      // Log the transaction
      transactionService.logTransaction(
        generateTransactionId(),
        loggedInCustomer.getCustomerId(),
        recipientId,
        amount
      );

      System.out.println(
        "Transfer successful! Updated balance: $" +
        transactionService.calculateAccountBalance(
          loggedInCustomer.getCustomerId(),
          accountsData,
          fileService.readTransactionsFromFile()
        )
      );
    } else {
      System.out.println("Recipient account not found.");
    }
  }

  private boolean isLoggedIn() {
    return loggedInCustomer != null;
  }

  private int generateTransactionId() {
    // This is a simple method to generate a unique transaction ID for demonstration purposes.
    return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
  }
}
