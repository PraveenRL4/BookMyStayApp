import java.util.LinkedList;
import java.util.Queue;

// I renamed these to 'UC5Reservation' to avoid the "Duplicate Class" error
// with your existing Reservation.java file.

class UC5Reservation {
    private String guestName;
    private String roomType;

    public UC5Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class UC5BookingQueue {
    private Queue<UC5Reservation> requestQueue = new LinkedList<>();

    public void addRequest(UC5Reservation reservation) { requestQueue.offer(reservation); }
    public UC5Reservation getNextRequest() { return requestQueue.poll(); }
    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

public class BookingRequestQueue {
    public static void main(String[] args) {
        System.out.println("Booking Request Queue");

        UC5BookingQueue bookingQueue = new UC5BookingQueue();

        bookingQueue.addRequest(new UC5Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new UC5Reservation("Subha", "Double"));
        bookingQueue.addRequest(new UC5Reservation("Vanmathi", "Suite"));

        while (bookingQueue.hasPendingRequests()) {
            UC5Reservation current = bookingQueue.getNextRequest();
            System.out.println("Processing booking for Guest: " + current.getGuestName() +
                    ", Room Type: " + current.getRoomType());
        }
    }
}