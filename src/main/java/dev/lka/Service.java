package dev.lka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import dev.lka.exception.CustomException;
import dev.lka.model.Booking;
import dev.lka.model.Room;
import dev.lka.model.RoomType;
import dev.lka.model.User;
import dev.lka.util.CustomSimpleDateFormat;

public class Service {
    ArrayList<Room> rooms;
    ArrayList<User> users;
    ArrayList<Booking> bookings;

    public Service(){
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight){
        Optional<Room> optionalRoom = findRoom(roomNumber);

        if (optionalRoom.isEmpty()){
            Room newRoom = new Room(roomNumber, roomType, roomPricePerNight);
            this.rooms.add(0, newRoom);
        }
        else{
            Room existedRoom = optionalRoom.get();
            int indexOfRoom = this.rooms.indexOf(existedRoom);

            existedRoom.setType(roomType);
            existedRoom.setPricePerNight(roomPricePerNight);

            this.rooms.set(indexOfRoom, existedRoom);
        }

    }

    void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut){

        Optional<Room> optionalRoom = findRoom(roomNumber);

        Optional<User> optionalUser = findUser(userId);

        if(optionalRoom.isEmpty()){
            throw new CustomException("This room does not exist");
        }
        if(optionalUser.isEmpty()){
            throw new CustomException("User does not exist");
        }

        // Normalize dates to only consider year, month, day (set time to 00:00:00)
        checkIn = normalizeDate(checkIn);
        checkOut = normalizeDate(checkOut);

        if(checkIn.after(checkOut) || checkIn.equals(checkOut)){
            throw new CustomException("CheckIn date must come before checkOut");
        }

        User user = optionalUser.get();
        Room room = optionalRoom.get();

        // Calculate number of nights
        long diffInMillis = checkOut.getTime() - checkIn.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
        int numberOfNights = (int) diffInDays;

        // Calculate total cost for the entire period
        int totalCost = room.getPricePerNight() * numberOfNights;

        if(user.getBalance() < totalCost){
            throw new CustomException("User balance is not enough. Required: " + totalCost + ", Actual: " + user.getBalance());
        }

        /***
         * A room cannot be booked if it is occupied
         * Check for overlapping date ranges
         */
        List<Booking> roomBookings = this.bookings.stream()
                .filter(booking -> booking.getRoom().getId() == roomNumber)
                .toList();

        // Check if the new booking overlaps with any existing booking
        for (Booking existingBooking : roomBookings) {
            Date existingCheckIn = normalizeDate(existingBooking.getDateCheckIn());
            Date existingCheckOut = normalizeDate(existingBooking.getDateCheckOut());

            // Check for overlap: new booking starts before existing ends AND new booking ends after existing starts
            if (checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn)) {
                throw new CustomException("This room is not available for this period. It is booked from " +
                    CustomSimpleDateFormat.format(existingCheckIn) + " to " +
                    CustomSimpleDateFormat.format(existingCheckOut));
            }
        }

        Room roomClone;
        try {
            roomClone = (Room) room.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        Booking newBooking = new Booking(
                Booking.NUMBER_OF_BOOKINGS,
                user,
                roomClone,
                checkIn,
                checkOut
        );

        bookings.add(0, newBooking);

        // Deduct total cost for the entire period
        user.setBalance(user.getBalance() - totalCost);
        setUser(user.getId(), user.getBalance());
    }

    void printAll(){
        System.out.println("********** Rooms list **********");
        this.rooms.forEach(room -> {
            System.out.println("- Id: " + room.getId() + "; Type: " + room.getType() + "; Price per night: " + room.getPricePerNight());
        });

        System.out.println("=======================================");
        System.out.println("********** Bookings list **********");
        this.bookings.forEach(booking -> {
            System.out.println("- Booking id: " + booking.getId() +
                    "; User: " + booking.getUser().getId() +
                    "; User Balance: " + booking.getUser().getBalance() +
                    "; Reserved room: " + booking.getRoom().getId() +
                    "; Reserved room type: " + booking.getRoom().getType() +
                    "; Reserved room price per night: " + booking.getRoom().getPricePerNight() +
                    "; Check in: " + CustomSimpleDateFormat.format(booking.getDateCheckIn()) +
                    "; Check out: " + CustomSimpleDateFormat.format(booking.getDateCheckOut()));
        });
    }

    void setUser(int userId, int balance){
        Optional<User> optionalUser = findUser(userId);

        if(optionalUser.isEmpty()){
            User newUser = new User(userId, balance);
            this.users.add(0, newUser);
        }
        else{
            User existedUsed = optionalUser.get();
            int userIndex = this.users.indexOf(existedUsed);

            existedUsed.setBalance(balance);

            this.users.set(userIndex, existedUsed);
        }
    }

    void printAllUsers(){
        System.out.println("*********** Users list **********");
        this.users.forEach(user -> {
            System.out.println("- User: " + user.getId() + "; Balance: " + user.getBalance());
        });
    }


    private Optional<User> findUser(int userId) {
        return this.users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst();
    }

    private Optional<Room> findRoom(int roomNumber) {
        return this.rooms.stream()
                .filter(room -> room.getId() == roomNumber)
                .findFirst();
    }

    /**
     * Normalize date to only consider year, month, and day (set time to 00:00:00)
     */
    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
