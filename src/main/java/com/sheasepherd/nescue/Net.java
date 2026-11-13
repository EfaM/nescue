package com.sheasepherd.nescue;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class Net {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Valid
    @NotNull
    private Coordinates coordinates;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name="assigned_person_id")
    private Person assignedPerson;

    protected Net(){}

    public Net(Coordinates coordinates, Size size, Status status) {
        this.coordinates = coordinates;
        this.size = size;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Size getSize() {
        return size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(Person assignedPerson) {
        this.assignedPerson = assignedPerson;
    }
}
