import java.util.*;

/**
 * ===================================================================
 * SUPPORTING CLASS - RoomInventory (Required for Use Case 6)
 * ===================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) {
        if (isAvailable(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }
}

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
 * CLASS - RoomAllocationService
 * ===================================================================
 */
class RoomAllocationService {
    /** Stores all allocated room IDs to prevent duplicate assignments. */
    private Set<String> allocatedRoomIds;

    /** Stores assigned room IDs by room type. Key: Room Type, Value: Set of IDs */
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /** Confirms a booking and assigns a unique room ID. */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String type = reservation.getRoomType();

        if (inventory.isAvailable(type)) {
            String roomId = generateRoomId(type);

            // Record allocation
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);

            // Update inventory immediately
            inventory.decrementInventory(type);

            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                    ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() +
                    " - No " + type + " rooms available.");
        }
    }

    /** Generates a unique room ID based on the count of rooms of that type. */
    private String generateRoomId(String roomType) {
        int currentCount = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size();
        return roomType + "-" + (currentCount + 1);
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ===================================================================
 */
public class RoomAllocation {
    public static void main(String[] args) {
        System.out.println("Room Allocation Processing");

        // 1. Initialize Services
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // 2. Simulate incoming requests (from Use Case 5)
        bookingQueue.offer(new Reservation("Abhi", "Single"));
        bookingQueue.offer(new Reservation("Subha", "Single"));
        bookingQueue.offer(new Reservation("Vanmathi", "Suite"));

        // 3. Process requests in FIFO order
        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            allocationService.allocateRoom(request, inventory);
        }
    }
}