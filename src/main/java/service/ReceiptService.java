// ReceiptService.java
package main.java.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import main.java.model.Account;
import main.java.model.Customer;
import main.java.model.Transaction;

public class ReceiptService {

  private String bankName;
  private String bankAddress;

  public ReceiptService(String bankName, String bankAddress) {
    this.bankName = bankName;
    this.bankAddress = bankAddress;
  }

  public String generateReceipt(
    Customer customer,
    int account,
    Transaction transaction
  ) {
    StringBuilder receipt = new StringBuilder();

    receipt.append("Bank Name: ").append(bankName).append("\n");
    receipt.append("Bank Address: ").append(bankAddress).append("\n");
    receipt.append("Customer Name: ").append(customer.getName()).append("\n");
    receipt
      .append("Customer Address: ")
      .append(customer.getAddress())
      .append("\n");
    receipt
      .append("Account Number: ")
      .append(account)
      .append("\n");
    receipt
      .append("Transaction Amount: ")
      .append(transaction.getTransactionAmount())
      .append("\n");
    receipt
      .append("Transaction Type: ")
      .append(transaction.getTransactionType())
      .append("\n");
    receipt
      .append("Transaction Date and Time: ")
      .append(transaction.getTransactionDate())
      .append("\n");

    return receipt.toString();
  }

  private String formatDate(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    return dateFormat.format(date);
  }
}
