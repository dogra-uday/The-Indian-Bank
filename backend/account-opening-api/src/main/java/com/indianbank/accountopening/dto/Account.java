package com.indianbank.accountopening.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Account {

  @NotBlank
  @Pattern(regexp = "Savings|Current|Salary")
  private String accountType;

  @NotNull
  @DecimalMin("1000")
  @DecimalMax("1000000")
  private BigDecimal openingDeposit;

  @NotBlank
  @Pattern(regexp = "Yes|No")
  private String debitCardRequired;

  private List<@Pattern(regexp = "Net Banking|Mobile Banking|SMS Alerts|Cheque Book") String> additionalServices;

  public Account() {
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public BigDecimal getOpeningDeposit() {
    return openingDeposit;
  }

  public void setOpeningDeposit(BigDecimal openingDeposit) {
    this.openingDeposit = openingDeposit;
  }

  public String getDebitCardRequired() {
    return debitCardRequired;
  }

  public void setDebitCardRequired(String debitCardRequired) {
    this.debitCardRequired = debitCardRequired;
  }

  public List<String> getAdditionalServices() {
    return additionalServices;
  }

  public void setAdditionalServices(List<String> additionalServices) {
    this.additionalServices = additionalServices;
  }
}
