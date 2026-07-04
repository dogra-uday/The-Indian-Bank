package com.indianbank.accountopening.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AccountOpeningSubmissionRequest {

  @NotNull
  @Valid
  private RequestContext requestContext;

  @NotNull
  @Valid
  private Applicant applicant;

  @NotNull
  @Valid
  private Contact contact;

  @NotNull
  @Valid
  private Account account;

  @Valid
  private Documents documents;

  @NotNull
  @Valid
  private Declaration declaration;

  public AccountOpeningSubmissionRequest() {
  }

  public RequestContext getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(RequestContext requestContext) {
    this.requestContext = requestContext;
  }

  public Applicant getApplicant() {
    return applicant;
  }

  public void setApplicant(Applicant applicant) {
    this.applicant = applicant;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Documents getDocuments() {
    return documents;
  }

  public void setDocuments(Documents documents) {
    this.documents = documents;
  }

  public Declaration getDeclaration() {
    return declaration;
  }

  public void setDeclaration(Declaration declaration) {
    this.declaration = declaration;
  }
}
