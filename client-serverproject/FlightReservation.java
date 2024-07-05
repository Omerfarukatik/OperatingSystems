package newproject;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class FlightReservation {
    private final Map<String, String> reservations = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void makeReservation(String seat, String customer, PrintWriter serverOut) {
        rwLock.writeLock().lock();
        try {
            if (!reservations.containsKey(seat)) {
                reservations.put(seat, customer);
                serverOut.println("Time: " + System.currentTimeMillis() + " " + customer + " reserved seat " + seat);
            } else {
                serverOut.println("Time: " + System.currentTimeMillis() + " " + customer + " tried to reserve seat " + seat + ", but it is already reserved by " + reservations.get(seat));
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void cancelReservation(String seat, String customer, PrintWriter serverOut) {
        rwLock.writeLock().lock();
        try {
            if (reservations.containsKey(seat) && reservations.get(seat).equals(customer)) {
                reservations.remove(seat);
                serverOut.println("Time: " + System.currentTimeMillis() + " " + customer + " cancelled reservation for seat " + seat);
            } else {
                serverOut.println("Time: " + System.currentTimeMillis() + " Seat " + seat + " is not reserved by " + customer);
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void queryReservation(PrintWriter serverOut) {
        rwLock.readLock().lock();
        try {
            serverOut.println("Time: " + System.currentTimeMillis() + " State of the seats are :");
            for (int i = 1; i <= 5; i++) {
                String seat = "1" + (char) ('A' + i - 1);
                serverOut.println("Seat " + seat + " : " + (reservations.containsKey(seat) ? reservations.get(seat) : "0"));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
