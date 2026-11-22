package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.model.Person;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.util.StringUtils;


public class AssignForm {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank @Pattern(regexp = "|" + Person.PHONE_REG)
    private String phoneNumber;

    public AssignForm() {}

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
