// Customer.java
package main.java.model;

public class Customer {

  private int customerId;
  private int pin;
  private String name;
  private String email;
  private String phone;
  private String address;

  public Customer(
    int customerId,
    int pin,
    String name,
    String email,
    String phone,
    String address
  ) {
    this.customerId = customerId;
    this.pin = pin;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  // Setter methods

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public void setPin(int pin) {
    this.pin = pin;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  // Getter methods

  public int getCustomerId() {
    return customerId;
  }

  public int getPin() {
    return pin;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getAddress() {
    return address;
  }
}
