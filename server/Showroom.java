package server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Singleton (enum style) class that holds the showroom.
 * @author Riadh Laabidi
 */
public enum Showroom {

    INSTANCE;

    private Map<Integer, Show> shows;

    private final Lock readLock;
    private final Lock writeLock;

    Showroom() {
        // create locks for reading and writing
        final ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

        // populate the showroom with some shows
        shows = new HashMap<>(5);
        shows.put(1, new Show("The Man Who Sold His Skin", 20));
        shows.put(2, new Show("The Flower of Aleppo", 25));
        shows.put(3, new Show("Beauty and the Dogs", 15));
        shows.put(4, new Show("As I Open My Eyes", 25));
    }

    /**
     * Returns the list of shows in the showroom.
     * @return a string representation of the list of shows
     */
    public String getShowsAsString() {
        try {
            // lock the showroom for reading
            readLock.lock();

            StringBuilder sb = new StringBuilder();
            shows.forEach((showId, show) -> sb
                    .append("\t")
                    .append(showId).append(": ").append(show)
                    .append("\n")
            );
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } finally {
            // unlock the showroom after reading
            readLock.unlock();
        }
    }

    /**
     * Books a number of seats in a specific show.
     * @param showId the id of the show to book seats in
     * @param numberOfPlaces number of seats to book
     * @return a message indicating the result
     */
    public String book(int showId, int numberOfPlaces) {
        try {
            //lock the showroom for writing
            writeLock.lock();

            if (numberOfPlaces <= 0) {
                return "Invalid number of places";
            }

            if (!shows.containsKey(showId)) {
                return "Show #" + showId + " does not exist";
            }

            Show show = shows.get(showId);

            if (show.getSeats() == 0) {
                return "Show #" + showId + " is full";
            }

            if (show.getSeats() - numberOfPlaces < 0) {
                return "Not enough seats, available: " + show.getSeats();
            }

            show.setSeats(show.getSeats() - numberOfPlaces);
            shows.put(showId, show);
            return "Booked " + numberOfPlaces + " seats for show #" + showId;
        } finally {
            // unlock the showroom after modification
            writeLock.unlock();
        }
    }
}
