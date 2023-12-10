// FileService.java
package main.java.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

  private static final String CUSTOMER_FILE =
    "src/main/resources/customers.txt";
  private static final String ACCOUNT_FILE = "src/main/resources/accounts.txt";
  private static final String TRANSACTION_FILE =
    "src/main/resources/transactions.txt";

  public List<String> readLinesFromFile(String filePath) {
    List<String> lines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      // reader.readLine(); // Skip the first line
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          lines.add(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }

  public void writeLinesToFile(List<String> lines, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<String> readCustomersFromFile() {
    System.out.println("Reading customers from file");
    return readLinesFromFile(CUSTOMER_FILE);
  }

  public void writeCustomersToFile(List<String> customers) {
    writeLinesToFile(customers, CUSTOMER_FILE);
  }

  public List<String> readAccountsFromFile() {
    return readLinesFromFile(ACCOUNT_FILE);
  }

  public void writeAccountsToFile(List<String> accounts) {
    writeLinesToFile(accounts, ACCOUNT_FILE);
  }

  public List<String> readTransactionsFromFile() {
    return readLinesFromFile(TRANSACTION_FILE);
  }

  public void writeTransactionsToFile(List<String> transactions) {
    writeLinesToFile(transactions, TRANSACTION_FILE);
  }
}
