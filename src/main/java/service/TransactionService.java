// TransactionService.java
package main.java.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.java.model.Account;
import main.java.model.Customer;
import main.java.model.Transaction;

public class TransactionService {

  private static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
  private FileService fileService;
  private ReceiptService receiptService;
  private AuthenticationService authenticationService;

  public TransactionService(
    FileService fileService,
    ReceiptService receiptService,
    AuthenticationService authenticationService
  ) {
    this.fileService = fileService;
    this.receiptService = receiptService;
    this.authenticationService = authenticationService;
  }

  public double calculateAccountBalance(
    int customerId,
    List<String> accountsData,
    List<String> transactionsData
  ) {
    double balance = 0.0;

    String customerAccount = getCustomerAccount(accountsData, customerId);

    if (customerAccount == null) {
      System.out.println("No account found for the logged-in customer.");
      return 0;
    }

    for (String accountData : accountsData) {
      String[] accountParts = accountData.split(",");
      int accountId = Integer.parseInt(accountParts[0]);
      int customerAccountId = Integer.parseInt(accountParts[1]);
      double accountBalance = Double.parseDouble(accountParts[5]);

      if (customerAccountId == customerId) {
        balance += accountBalance;
      }
    }

    return balance;
  }

  private String getCustomerAccount(List<String> accountsData, int customerId) {
    for (String accountData : accountsData) {
      String[] accountParts = accountData.split(",");
      int customerAccountId = Integer.parseInt(accountParts[1]);

      if (customerAccountId == customerId) {
        return accountData;
      }
    }

    return null;
  }

  private int extractAccountId(String accountData) {
    String[] accountParts = accountData.split(",");
    return Integer.parseInt(accountParts[0]); // Assuming account ID is the first part
  }

  public void processDeposit(
    int customerId,
    int toAccountId,
    double amount,
    List<String> accountsData
  ) {
    // Find the target account in the accountsData
    for (int i = 0; i < accountsData.size(); i++) {
      String accountData = accountsData.get(i);
      String[] accountParts = accountData.split(",");
      int currentAccountId = Integer.parseInt(accountParts[0]);

      if (currentAccountId == toAccountId) {
        double currentBalance = Double.parseDouble(accountParts[5]);
        double newBalance = currentBalance + amount;
        customerId = Integer.parseInt(accountParts[1]);
        accountParts[5] = String.valueOf(newBalance);
        accountsData.set(i, String.join(",", accountParts));
        break;
      }
    }

    // Update the accounts file
    fileService.writeAccountsToFile(accountsData);

    generateAndPrintReceipt(customerId, toAccountId, "Deposit", amount);
  }

  private Customer getCustomerFromData(
    int customerId,
    List<String> customersData
  ) {
    for (String customerData : customersData) {
      String[] customerParts = customerData.split(",");
      int currentCustomerId = Integer.parseInt(customerParts[0]);
      int currentPin = Integer.parseInt(customerParts[1]);

      if (currentCustomerId == customerId) {
        return new Customer(
          currentCustomerId,
          currentPin,
          customerParts[2],
          customerParts[3],
          customerParts[4],
          customerParts[5]
        );
      }
    }

    return null;
  }

  private void generateAndPrintReceipt(
    int fromCustomerId,
    int toAccountId,
    String transactionType,
    double amount
  ) {
    // Retrieve customer and account details (replace with actual logic)
    Customer customer = authenticationService.getCustomerById(fromCustomerId);

    // if customer id is null, then create a new customer
    if (customer == null) {
      List<String> customersData = fileService.readCustomersFromFile();
      customer = getCustomerFromData(fromCustomerId, customersData);
    }

    float amounts = (float) amount;

    Transaction transaction = new Transaction(
      toAccountId,
      amounts,
      transactionType,
      getCurrentDateTime()
    );

    // Generate and print the receipt
    String receipt = receiptService.generateReceipt(
      customer,
      toAccountId,
      transaction
    );

    System.out.println("\n\nReceipt:--------------------------------\n");
    System.out.println(receipt);
    System.out.println("\nEnd of receipt:--------------------------------\n\n");
  }

  private String getCurrentDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    return dateFormat.format(new Date());
  }

  public void processWithdrawal(
    int customerId,
    int accountId,
    double amount,
    List<String> accountsData
  ) {
    for (int i = 0; i < accountsData.size(); i++) {
      String accountData = accountsData.get(i);
      String[] accountParts = accountData.split(",");
      int currentAccountId = Integer.parseInt(accountParts[0]);

      if (currentAccountId == accountId) {
        double currentBalance = Double.parseDouble(accountParts[5]);
        double newBalance = currentBalance - amount;
        customerId = Integer.parseInt(accountParts[1]);
        accountParts[5] = String.valueOf(newBalance);
        accountsData.set(i, String.join(",", accountParts));
        break;
      }
    }

    fileService.writeAccountsToFile(accountsData);
    generateAndPrintReceipt(customerId, accountId, "Withdraw", amount);
  }

  public void processTransfer(
    int customerId,
    int fromAccountId,
    int toAccountId,
    double amount,
    List<String> accountsData
  ) {
    processWithdrawal(customerId, fromAccountId, amount, accountsData);
    processDeposit(customerId, toAccountId, amount, accountsData);
  }

  public void logTransaction(
    int transactionId,
    int fromAccountId,
    int toAccountId,
    double amount
  ) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    String transactionDate = dateFormat.format(new Date());

    String transactionRecord = String.format(
      "%d,%d,%d,%.2f,%s",
      transactionId,
      fromAccountId,
      toAccountId,
      amount,
      transactionDate
    );
    List<String> transactionsData = fileService.readTransactionsFromFile();
    transactionsData.add(transactionRecord);
    fileService.writeTransactionsToFile(transactionsData);
  }
}
