package dev.lka.model;

public class User {
    private int id;
    private int balance;

    public User(){this(0);}

    public User(int balance) {
        this.balance = balance;
    }

    public User(int id, int balance){
        this.id = id;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "balance=" + balance +
                ", id=" + id +
                '}';
    }
}
