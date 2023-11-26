package com.mastering.lambdas.designpatterns.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Example 1
 */
public class BankAccount {
    private int balance;
    private int overdraftLimit = -500;

    public void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited " + amount + ", balance is now " + balance);
    }

    public boolean withdraw(int amount) {
        if (balance - amount >= overdraftLimit) {
            balance -= amount;
            System.out.println("Withdrew " + amount + ", balance is now " + balance);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                '}';
    }
}

interface Command {
    void call();

    void undo();
}

class BankAccountCommand implements Command {
    private BankAccount account;
    private Action action;
    private int amount;

    private boolean succeeded;

    public enum Action {DEPOSIT, WITHDRAW}

    public BankAccountCommand(BankAccount account, Action action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    @Override
    public void call() {
        switch (action) {
            case DEPOSIT:
                succeeded = true;
                account.deposit(amount);
                break;
            case WITHDRAW:
                succeeded = account.withdraw(amount);
                break;
        }
    }

    @Override
    public void undo() {
        if (!succeeded) return;
        switch (action) {
            case DEPOSIT:
                account.withdraw(amount);
                break;
            case WITHDRAW:
                account.deposit(amount);
                break;
        }
    }
}

class Demo2 {
    public static void main(String[] args) {
        BankAccount ba = new BankAccount();
        System.out.println(ba);

        List<BankAccountCommand> commands = Stream.of(
                        new BankAccountCommand(ba, BankAccountCommand.Action.DEPOSIT, 100),
                        new BankAccountCommand(ba, BankAccountCommand.Action.WITHDRAW, 30)
                )
                .collect(Collectors.toList());
        commands.forEach(c -> {
            c.call();
            System.out.println(ba);
        });
        System.out.println("--------------------------");

        Collections.reverse(commands);
        for (Command c : commands) {
            c.undo();
            System.out.println(ba);
        }
    }
}

/**
 * Example 2
 */
class Command2 {
    enum Action {
        DEPOSIT, WITHDRAW
    }

    public Action action;
    public int amount;
    public boolean success;

    public Command2(Action action, int amount) {
        this.action = action;
        this.amount = amount;
    }
}

class Account {
    public int balance;

    public void process(Command2 c) {
        // todo
        switch (c.action) {
            case DEPOSIT:
                c.success = true;
                this.balance += c.amount;
                System.out.println("Deposited " + c.amount + "$. Balance is now " + this.balance + "$");
                break;
            case WITHDRAW:
                if (this.balance - c.amount < 0) {
                    c.success = false;
                    System.out.println("Attempt to withdraw " + c.amount + "$ failed because account balance is " + this.balance + "$");
                    return;
                }
                c.success = true;
                this.balance -= c.amount;
                System.out.println("Withdrew " + c.amount + "$. Balance is now " + this.balance + "$");
                break;
        }
    }

    public static void main(String[] args) {
        Command2 command1 = new Command2(Command2.Action.DEPOSIT, 100);
        Command2 command2 = new Command2(Command2.Action.WITHDRAW, 101);
        Command2 command3 = new Command2(Command2.Action.WITHDRAW, 51);
        Account account = new Account();
        account.process(command1);
        account.process(command2);
        account.process(command3);
        account.process(command3);
        System.out.println("Final account balance is: " + account.balance + "$");
    }
}

/**
 * Example 3
 */
interface Command3 {
    void execute();
}

class DomesticEngineer implements Command3 {
    public void execute() {
        System.out.println("take out the trash");
    }
}

class Politician implements Command3 {
    public void execute() {
        System.out.println("take money from the rich, take votes from the poor");
    }
}

class Programmer implements Command3 {
    public void execute() {
        System.out.println("sell the bugs, charge extra for the fixes");
    }
}

class CommandDemo {
    public static List produceRequests() {
        List<Command3> queue = new ArrayList<>();
        queue.add(new DomesticEngineer());
        queue.add(new Politician());
        queue.add(new Programmer());
        return queue;
    }

    public static void workOffRequests(List queue) {
        for (Object command : queue) {
            ((Command3) command).execute();
        }
    }

    public static void main(String[] args) {
        List queue = produceRequests();
        workOffRequests(queue);
    }
}
