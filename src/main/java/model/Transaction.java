// Transaction.java
package main.java.model;

import java.util.Date;

public class Transaction {

  private int accountNumber;
  private float transactionAmount;
  private String transactionType; // "Deposit" or "Withdraw"
  private String transactionDate;

  public Transaction(
    int accountNumber,
    float transactionAmount,
    String transactionType,
    String transactionDate
  ) {
    this.accountNumber = accountNumber;
    this.transactionAmount = transactionAmount;
    this.transactionType = transactionType;
    this.transactionDate = transactionDate;
  }

  // Setter methods

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setTransactionAmount(float transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public void setTransactionDate(String transactionString) {
    this.transactionDate = transactionDate;
  }

  // Getter methods

  public int getAccountNumber() {
    return accountNumber;
  }

  public float getTransactionAmount() {
    return transactionAmount;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getTransactionDate() {
    return transactionDate;
  }
}
