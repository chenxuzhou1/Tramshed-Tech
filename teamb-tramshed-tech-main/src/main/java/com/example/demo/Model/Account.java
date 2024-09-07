package com.example.demo.Model;

public class Account {

    private String fullName;
    private String accountName;
    private String gender;
    private String email;
    private String password;
    private String phoneNumber;
    private String location;
    private String dateOfBirth;
    private String companyName;
    private String billingAddress;
    private String billingCountry;
    private String billingState;
    private String billingCity;
    private String zipPostcode;
    private String sendMyInvoicesTo;

    // Constructors, Getters, and Setters
    public Account() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getZipPostcode() {
        return zipPostcode;
    }

    public void setZipPostcode(String zipPostcode) {
        this.zipPostcode = zipPostcode;
    }

    public String getSendMyInvoicesTo() {
        return sendMyInvoicesTo;
    }

    public void setSendMyInvoicesTo(String sendMyInvoicesTo) {
        this.sendMyInvoicesTo = sendMyInvoicesTo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "fullName='" + fullName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location='" + location + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", companyName='" + companyName + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", billingCountry='" + billingCountry + '\'' +
                ", billingState='" + billingState + '\'' +
                ", billingCity='" + billingCity + '\'' +
                ", zipPostcode='" + zipPostcode + '\'' +
                ", sendMyInvoicesTo='" + sendMyInvoicesTo + '\'' +
                '}';
    }
}
