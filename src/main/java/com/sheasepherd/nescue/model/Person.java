package com.sheasepherd.nescue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
public class Person {

    public static final String PHONE_REG = "^$|^\\+[1-9]\\d{1,14}$"; //E.164 Format nach Quelle: https://ihateregex.io/expr/e164-phone

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column (nullable = false)
    private String firstname;

    @NotBlank
    @Column (nullable = false)
    private String lastname;

    @NotBlank
    @Column (nullable = false, unique = true, length = 17)
    @Pattern (regexp = PHONE_REG)
    private String phoneNumber;

    protected Person() {}

    public Person (String firstname, String lastname, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
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

    public void setLastname(String surname) {
        this.lastname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
