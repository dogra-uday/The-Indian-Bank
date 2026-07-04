package com.indianbank.accountopening.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Contact {

  @NotBlank
  @Pattern(regexp = "^[6-9][0-9]{9}$")
  private String mobileNumber;

  @NotBlank
  @Email
  private String emailAddress;

  @NotBlank
  @Size(min = 5, max = 120)
  private String addressLine1;

  @Size(max = 120)
  private String addressLine2;

  @NotBlank
  @Size(max = 60)
  private String city;

  @NotBlank
  @Size(max = 60)
  private String state;

  @NotBlank
  @Pattern(regexp = "^[1-9][0-9]{5}$")
  private String pinCode;

  public Contact() {
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }
}
