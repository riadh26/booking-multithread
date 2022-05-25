package server;

/**
 * A class that models a show.
 */
public class Show {
    private String name;
    private int seats;

    public Show(String name, int seats) {
        this.name = name;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "(name: " + name + ", seats: " + seats + ")";
    }
}
