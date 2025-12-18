package dev.lka.model;

import java.time.LocalDate;
import java.util.Date;

public class Booking {
    private int id;
    private Room room;
    private User user;
    private Date dateCheckIn;
    private Date dateCheckOut;

    // TODO: find a better name
    public static int NUMBER_OF_BOOKINGS = 1;

    public Booking(int id, User user, Room room, Date dateCheckIn, Date dateCheckOut) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;

        NUMBER_OF_BOOKINGS += 1;
    }

    public Date getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(Date dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public Date getDateCheckOut() {
        return dateCheckOut;
    }

    public void setDateCheckOut(Date dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
    }

    public int getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
