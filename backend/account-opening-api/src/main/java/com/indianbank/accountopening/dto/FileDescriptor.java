package com.indianbank.accountopening.dto;

import javax.validation.constraints.Pattern;

public class FileDescriptor {

  private String fileName;
  private String contentType;

  @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "checksumSha256 must be a 64-character hex string")
  private String checksumSha256;

  public FileDescriptor() {
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getChecksumSha256() {
    return checksumSha256;
  }

  public void setChecksumSha256(String checksumSha256) {
    this.checksumSha256 = checksumSha256;
  }
}
