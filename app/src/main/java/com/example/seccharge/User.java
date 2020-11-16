package com.example.seccharge;

import java.util.Map;

public class User {

    User(String username, String password, boolean active, boolean locked, boolean twoFactorAuthEnabled, String firstName, String lastName, String companyName, String email, String dateCreated, String expiryDate, String address1, String address2, String city, String province, String postalCode, String country, String homePhone, String cellPhone, String fax, String formattedAddress, Map<String, String> securityQuestions) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.locked = locked;
        this.twoFactorAuthEnabled = twoFactorAuthEnabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.expiryDate = expiryDate;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
        this.fax = fax;
        this.formattedAddress = formattedAddress;
        this.securityQuestions = securityQuestions;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public boolean isTwoFactorAuthEnabled() {
        return twoFactorAuthEnabled;
    }
    public void setTwoFactorAuthEnabled(boolean twoFactorAuthEnabled) {
        this.twoFactorAuthEnabled = twoFactorAuthEnabled;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getHomePhone() {
        return homePhone;
    }
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }
    public String getCellPhone() {
        return cellPhone;
    }
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getFormattedAddress() {
        return formattedAddress;
    }
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    private String username;
    private String password;
    private boolean active;
    private boolean locked;
    private boolean twoFactorAuthEnabled ;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String dateCreated;
    private String expiryDate;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String homePhone;
    private String cellPhone ;
    private String fax;
    private String formattedAddress;
    private Map<String, String> securityQuestions;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(Map<String, String> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
}

