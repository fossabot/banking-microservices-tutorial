package com.ultimatesoftware.banking.account.cmd.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class CreditAccountCommand extends TransactionCommand implements ICommand {
    @TargetAggregateIdentifier
    private UUID id;
    private double amount;

    public CreditAccountCommand(UUID id, double balance, String transactionId) {
        super(transactionId);
        this.id = id;
        this.amount = balance;
    }

    public UUID getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
