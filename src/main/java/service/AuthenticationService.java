// AuthenticationService.java
package main.java.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.java.model.Customer;

public class AuthenticationService {

  private Map<Integer, Customer> customers; // CustomerID -> Customer
  private Map<Integer, Integer> failedAttempts; // CustomerID -> Failed Attempt Count
  private final int MAX_ATTEMPTS = 3; // Maximum number of allowed login attempts

  public AuthenticationService() {
    this.customers = new HashMap<>();
    this.failedAttempts = new HashMap<>();
    // For simplicity, we'll manually add some dummy data.
    initializeDummyData();
  }

  private void initializeDummyData() {
    Customer customer1 = new Customer(
      12345,
      5678,
      "John Doe",
      "john.doe@example.com",
      "123-456-7890",
      "123 Main St"
    );
    Customer customer2 = new Customer(
      67890,
      9876,
      "Jane Smith",
      "jane.smith@example.com",
      "987-654-3210",
      "456 Oak St"
    );

    customers.put(customer1.getCustomerId(), customer1);
    customers.put(customer2.getCustomerId(), customer2);
  }

  public boolean authenticateCustomer(
    int customerId,
    int pin,
    List<String> customersData
  ) {
    if (
      failedAttempts.containsKey(customerId) &&
      failedAttempts.get(customerId) >= MAX_ATTEMPTS
    ) {
      System.out.println("Account locked. Too many failed attempts.");
      System.exit(0);

      return false;
    }

    Customer customer = authenticate(customerId, pin, customersData);

    if (customer != null && customer.getPin() == pin) {
      resetFailedAttempts(customerId);
      customers.put(customer.getCustomerId(), customer);
      return true;
    } else {
      incrementFailedAttempts(customerId);
      return false;
    }
  }

  public Customer authenticate(
    int customerId,
    int pin,
    List<String> customersData
  ) {
    for (String customerData : customersData) {
      String[] customerParts = customerData.split(",");
      int currentCustomerId = Integer.parseInt(customerParts[0]);
      int currentPin = Integer.parseInt(customerParts[1]);

      if (currentCustomerId == customerId && currentPin == pin) {
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

  private void incrementFailedAttempts(int customerId) {
    failedAttempts.put(
      customerId,
      failedAttempts.getOrDefault(customerId, 0) + 1
    );
    System.out.println(
      "Incorrect CustomerId or PIN. Attempts left: " +
      (MAX_ATTEMPTS - failedAttempts.getOrDefault(customerId, 0))
    );
  }

  private void resetFailedAttempts(int customerId) {
    failedAttempts.remove(customerId);
  }

  public Customer getCustomerById(int customerId) {
    return customers.get(customerId);
  }
}
