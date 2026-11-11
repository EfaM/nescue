package com.sheasepherd.nescue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
public class Person {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column (nullable = false)
    private String firstname;

    @NotBlank
    @Column (nullable = false)
    private String surname;

    @NotBlank
    @Column (nullable = false, unique = true, length = 17)
    @Pattern (regexp = "^\\+[1-9]\\d{1,14}$") //E.164 Format - Quelle: https://ihateregex.io/expr/e164-phone
    private String phoneNumber;

    protected Person() {}

    public Person (String firstname, String surname, String phoneNumber) {
        this.firstname = firstname;
        this.surname = surname;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
