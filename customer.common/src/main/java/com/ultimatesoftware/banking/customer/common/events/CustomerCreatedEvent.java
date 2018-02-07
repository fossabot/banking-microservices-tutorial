package com.ultimatesoftware.banking.customer.common.events;

import java.util.UUID;

public class CustomerCreatedEvent {
    private UUID id;
    private String firstName = "";
    private String lastName = "";

    public CustomerCreatedEvent(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
