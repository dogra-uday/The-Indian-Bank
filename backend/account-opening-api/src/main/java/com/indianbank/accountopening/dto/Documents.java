package com.indianbank.accountopening.dto;

import javax.validation.Valid;

public class Documents {

  @Valid
  private FileDescriptor passportPhoto;

  @Valid
  private FileDescriptor addressProof;

  public Documents() {
  }

  public FileDescriptor getPassportPhoto() {
    return passportPhoto;
  }

  public void setPassportPhoto(FileDescriptor passportPhoto) {
    this.passportPhoto = passportPhoto;
  }

  public FileDescriptor getAddressProof() {
    return addressProof;
  }

  public void setAddressProof(FileDescriptor addressProof) {
    this.addressProof = addressProof;
  }
}
