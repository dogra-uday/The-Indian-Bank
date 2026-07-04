package com.indianbank.accountopening.dto;

public class SubmissionAcceptedResponse {

  private String applicationId;
  private String status;
  private String receivedAt;
  private String nextAction;

  public SubmissionAcceptedResponse() {
  }

  public SubmissionAcceptedResponse(String applicationId, String status, String receivedAt, String nextAction) {
    this.applicationId = applicationId;
    this.status = status;
    this.receivedAt = receivedAt;
    this.nextAction = nextAction;
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

  public String getReceivedAt() {
    return receivedAt;
  }

  public void setReceivedAt(String receivedAt) {
    this.receivedAt = receivedAt;
  }

  public String getNextAction() {
    return nextAction;
  }

  public void setNextAction(String nextAction) {
    this.nextAction = nextAction;
  }
}
