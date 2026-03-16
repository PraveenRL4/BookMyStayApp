import java.util.*;

/**
 * ===================================================================
 * CLASS - Reservation
 * ===================================================================
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * ===================================================================
 * CLASS - RoomInventory (Thread-Safe Access)
 * ===================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void showInventory() {
        System.out.println("\nRemaining Inventory:");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count));
    }
}

/**
 * ===================================================================
 * CLASS - BookingRequestQueue
 * ===================================================================
 */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation res) { requestQueue.offer(res); }
    public Reservation getNextRequest() { return requestQueue.poll(); }
    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

/**
 * ===================================================================
 * CLASS - RoomAllocationService
 * ===================================================================
 */
class RoomAllocationService {
    private Map<String, Integer> allocationCount = new HashMap<>();

    public void allocateRoom(Reservation res, RoomInventory inventory) {
        String type = res.getRoomType();
        if (inventory.isAvailable(type)) {
            inventory.decrementInventory(type);
            int count = allocationCount.getOrDefault(type, 0) + 1;
            allocationCount.put(type, count);
            System.out.println("Booking confirmed for Guest: " + res.getGuestName() +
                    ", Room ID: " + type + "-" + count);
        }
    }
}

/**
 * ===================================================================
 * CLASS - ConcurrentBookingProcessor
 * ===================================================================
 */
class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation = null;

            // Synchronize on the queue to ensure only one thread retrieves a request
            synchronized (bookingQueue) {
                if (bookingQueue.hasPendingRequests()) {
                    reservation = bookingQueue.getNextRequest();
                } else {
                    break; // No more requests, exit thread
                }
            }

            if (reservation != null) {
                // Synchronize on inventory to ensure atomic allocation
                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }

            // Small sleep to simulate processing time and increase chance of interleaving
            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
        }
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ===================================================================
 */
public class ConcurrentBookingSimulation {
    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation");

        // Initialize shared resources
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add sample requests to the shared queue
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create booking processor tasks
        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService);

        // Start multiple threads
        Thread t1 = new Thread(processor);
        Thread t2 = new Thread(processor);

        t1.start();
        t2.start();

        try {
            // Wait for both threads to finish
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Display final state
        inventory.showInventory();
    }
}