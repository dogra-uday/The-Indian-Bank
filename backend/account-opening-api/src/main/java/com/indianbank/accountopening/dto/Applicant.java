package com.indianbank.accountopening.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Applicant {

  @NotBlank
  @Size(min = 2, max = 50)
  private String firstName;

  @NotBlank
  @Size(min = 1, max = 50)
  private String lastName;

  @NotBlank
  private String dateOfBirth;

  private String gender;
  private String maritalStatus;

  @NotBlank
  @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
  private String panNumber;

  @NotBlank
  @Pattern(regexp = "^[2-9][0-9]{11}$")
  private String aadhaarNumber;

  public Applicant() {
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public String getPanNumber() {
    return panNumber;
  }

  public void setPanNumber(String panNumber) {
    this.panNumber = panNumber;
  }

  public String getAadhaarNumber() {
    return aadhaarNumber;
  }

  public void setAadhaarNumber(String aadhaarNumber) {
    this.aadhaarNumber = aadhaarNumber;
  }
}
