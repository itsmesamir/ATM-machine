// Account.java
package main.java.model;

import java.util.Date;

public class Account {

  private int accountNumber;
  private int accountType; // 1: Checking, 2: Savings
  private Date openDate;
  private float balance;
  private int customerId;

  public Account(
    int accountNumber,
    int accountType,
    Date openDate,
    float balance,
    int customerId
  ) {
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.openDate = openDate;
    this.balance = balance;
    this.customerId = customerId;
  }

  // Setter methods

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setAccountType(int accountType) {
    this.accountType = accountType;
  }

  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

  public void setBalance(float balance) {
    this.balance = balance;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  // Getter methods

  public int getAccountNumber() {
    return accountNumber;
  }

  public int getAccountType() {
    return accountType;
  }

  public Date getOpenDate() {
    return openDate;
  }

  public float getBalance() {
    return balance;
  }

  public int getCustomerId() {
    return customerId;
  }
}
