package com.indianbank.accountopening.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RequestContext {

  @NotBlank
  @Pattern(regexp = "AEM-EDGE-FORMS")
  private String sourceSystem;

  @NotBlank
  @Pattern(regexp = "WEB")
  private String sourceChannel;

  @NotBlank
  private String submittedAt;

  private String formVersion;
  private String journeyId;

  @Size(max = 32)
  private String branchCode;

  public RequestContext() {
  }

  public String getSourceSystem() {
    return sourceSystem;
  }

  public void setSourceSystem(String sourceSystem) {
    this.sourceSystem = sourceSystem;
  }

  public String getSourceChannel() {
    return sourceChannel;
  }

  public void setSourceChannel(String sourceChannel) {
    this.sourceChannel = sourceChannel;
  }

  public String getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(String submittedAt) {
    this.submittedAt = submittedAt;
  }

  public String getFormVersion() {
    return formVersion;
  }

  public void setFormVersion(String formVersion) {
    this.formVersion = formVersion;
  }

  public String getJourneyId() {
    return journeyId;
  }

  public void setJourneyId(String journeyId) {
    this.journeyId = journeyId;
  }

  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }
}
