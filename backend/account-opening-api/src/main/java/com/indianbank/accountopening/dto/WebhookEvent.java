package com.indianbank.accountopening.dto;

import javax.validation.constraints.NotBlank;

public class WebhookEvent {

  @NotBlank
  private String eventType;

  @NotBlank
  private String eventVersion;

  @NotBlank
  private String eventTime;

  @NotBlank
  private String correlationId;

  @NotBlank
  private String applicationId;

  @NotBlank
  private String status;

  private String subStatus;
  private String remarks;

  public WebhookEvent() {
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getEventVersion() {
    return eventVersion;
  }

  public void setEventVersion(String eventVersion) {
    this.eventVersion = eventVersion;
  }

  public String getEventTime() {
    return eventTime;
  }

  public void setEventTime(String eventTime) {
    this.eventTime = eventTime;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
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

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}
