package com.indianbank.accountopening.dto;

import java.util.List;

public class ErrorResponse {

  private String code;
  private String message;
  private String correlationId;
  private List<ErrorDetail> details;

  public ErrorResponse() {
  }

  public ErrorResponse(String code, String message, String correlationId, List<ErrorDetail> details) {
    this.code = code;
    this.message = message;
    this.correlationId = correlationId;
    this.details = details;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public List<ErrorDetail> getDetails() {
    return details;
  }

  public void setDetails(List<ErrorDetail> details) {
    this.details = details;
  }
}
