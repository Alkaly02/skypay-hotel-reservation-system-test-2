package dev.lka.model;

public class Room implements Cloneable {
    private int id;
    private RoomType type;
    private int pricePerNight;

    public Room(int id, RoomType type, int pricePerNight) {
        this.id = id;
        this.pricePerNight = pricePerNight;
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public int getId() {
        return id;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }
}
