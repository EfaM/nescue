package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.model.Person;
import com.sheasepherd.nescue.model.Size;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ReportForm {
    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private Size size;

    private String firstname;

    private String lastname;

    @Pattern(regexp = "|" + Person.PHONE_REG)
    private String phoneNumber;

    @AssertTrue
    public boolean isCompleteContactDataInForm(){
        if (StringUtils.hasText(firstname) && StringUtils.hasText(lastname) && StringUtils.hasText(phoneNumber)){ //Alle Kontaktdaten
            return true;
        } else if (!StringUtils.hasText(firstname) && !StringUtils.hasText(lastname) && !StringUtils.hasText(phoneNumber)) { //Keine Kontaktdaten enthalten
            return true;
        }
        return false;
    }

    public ReportForm(){}

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
