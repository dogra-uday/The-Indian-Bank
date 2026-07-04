package com.indianbank.accountopening.dto;

public class SubmissionStatusResponse {

  private String applicationId;
  private String status;
  private String subStatus;
  private String lastUpdatedAt;
  private String remarks;

  public SubmissionStatusResponse() {
  }

  public SubmissionStatusResponse(String applicationId, String status, String subStatus, String lastUpdatedAt, String remarks) {
    this.applicationId = applicationId;
    this.status = status;
    this.subStatus = subStatus;
    this.lastUpdatedAt = lastUpdatedAt;
    this.remarks = remarks;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSubStatus() {
    return subStatus;
  }

  public void setSubStatus(String subStatus) {
    this.subStatus = subStatus;
  }

  public String getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public void setLastUpdatedAt(String lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}
