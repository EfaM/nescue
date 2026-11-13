package com.sheasepherd.nescue;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
public class NetEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Valid
    private Coordinates coordinates;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event event;

    @NotNull
    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    @ManyToOne
    @NotNull
    @JoinColumn(name ="net_id", nullable = false)
    private Net net;

    protected NetEvent(){}

    public NetEvent(Net net, Person person, Instant timestamp, Event event, Coordinates coordinates) {
        this.net = net;
        this.person = person;
        this.timestamp = timestamp;
        this.event = event;
        this.coordinates = coordinates;
    }

    public UUID getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Event getEvent() {
        return event;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Person getPerson() {
        return person;
    }

    public Net getNet() {
        return net;
    }
}
