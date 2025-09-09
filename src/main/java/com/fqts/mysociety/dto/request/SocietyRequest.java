package com.fqts.mysociety.dto.request;

public class SocietyRequest {

  private String societyName;
  private String registrationNumber;
  private String detailedAddress;
  private String landmark;
  private Long cityId;
  private String zone;
  private String pincode;

  public String getSocietyName() {
    return societyName;
  }

  public void setSocietyName(String societyName) {
    this.societyName = societyName;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public String getDetailedAddress() {
    return detailedAddress;
  }

  public void setDetailedAddress(String detailedAddress) {
    this.detailedAddress = detailedAddress;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }


  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }
}


