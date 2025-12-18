package dev.lka;

import dev.lka.exception.CustomException;
import dev.lka.model.RoomType;
import dev.lka.util.CustomSimpleDateFormat;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        Service service = new Service();

        service.setRoom(1, RoomType.standard, 1000);
        service.setRoom(2, RoomType.junior, 2000);
        service.setRoom(3, RoomType.master, 3000);

        service.setUser(1, 5000);
        service.setUser(2, 10000);

        try{
            service.bookRoom(1, 2, CustomSimpleDateFormat.parse("30/06/2026"), CustomSimpleDateFormat.parse("07/07/2026"));
        }catch (CustomException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        try{
            service.bookRoom(1, 2, CustomSimpleDateFormat.parse("07/07/2026"), CustomSimpleDateFormat.parse("30/06/2026"));
        }catch (CustomException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        try{
            // Good test
            service.bookRoom(1, 1, CustomSimpleDateFormat.parse("07/07/2026"), CustomSimpleDateFormat.parse("08/07/2026"));
        }catch (CustomException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        try{
            // overlap
            service.bookRoom(2, 1, CustomSimpleDateFormat.parse("07/07/2026"), CustomSimpleDateFormat.parse("09/07/2026"));
        }catch (CustomException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        try{
            // Good test
            service.bookRoom(2, 3, CustomSimpleDateFormat.parse("07/07/2026"), CustomSimpleDateFormat.parse("08/07/2026"));
        }catch (CustomException ex){
            System.out.println("Error: " + ex.getMessage());
        }

        service.setRoom(1, RoomType.master, 10000);

        service.printAll();
        service.printAllUsers();
    }
}
