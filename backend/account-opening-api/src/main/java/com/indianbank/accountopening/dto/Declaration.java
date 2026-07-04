package com.indianbank.accountopening.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class Declaration {

  @NotNull
  @AssertTrue(message = "consentAccepted must be true")
  private Boolean consentAccepted;

  private String consentTimestamp;

  public Declaration() {
  }

  public Boolean getConsentAccepted() {
    return consentAccepted;
  }

  public void setConsentAccepted(Boolean consentAccepted) {
    this.consentAccepted = consentAccepted;
  }

  public String getConsentTimestamp() {
    return consentTimestamp;
  }

  public void setConsentTimestamp(String consentTimestamp) {
    this.consentTimestamp = consentTimestamp;
  }
}
