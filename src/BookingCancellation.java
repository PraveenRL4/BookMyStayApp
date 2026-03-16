import java.util.*;

/**
 * ===================================================================
 * SUPPORTING CLASS - RoomInventory
 * ===================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        // Sample data to demonstrate rollback
        inventory.put("Single", 5);
        inventory.put("Double", 3);
    }

    public void incrementInventory(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/**
 * ===================================================================
 * CLASS - CancellationService
 * ===================================================================
 */
class CancellationService {
    /** Stack that stores recently released room IDs. (LIFO) */
    private Stack<String> releasedRoomIds;

    /** Maps reservation ID to room type for lookup during cancellation. */
    private Map<String, String> reservationRoomTypeMap;

    /** Initializes cancellation tracking structures. */
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking for future cancellation support.
     * @param reservationId confirmed reservation ID
     * @param roomType allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and restores inventory safely.
     * @param reservationId reservation to cancel
     * @param inventory centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            String roomType = reservationRoomTypeMap.get(reservationId);

            // 1. Record released ID in the stack (Rollback History)
            releasedRoomIds.push(reservationId);

            // 2. Restore inventory immediately
            inventory.incrementInventory(roomType);

            // 3. Remove from active map
            reservationRoomTypeMap.remove(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        } else {
            System.out.println("Cancellation failed: Reservation ID not found.");
        }
    }

    /**
     * Displays recently cancelled reservations.
     * This method helps visualize rollback order (LIFO).
     */
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations recorded.");
        } else {
            // Stack displays LIFO order
            for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
                System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
            }
        }
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ===================================================================
 */
public class BookingCancellation {

    public static void main(String[] args) {
        // Display application header
        System.out.println("Booking Cancellation");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // 1. Setup: Simulate a confirmed booking (Single-1)
        String reservationId = "Single-1";
        String roomType = "Single";
        cancellationService.registerBooking(reservationId, roomType);

        // 2. Perform Cancellation
        cancellationService.cancelBooking(reservationId, inventory);

        // 3. Show Rollback History (LIFO)
        cancellationService.showRollbackHistory();

        // 4. Verify Updated Inventory
        System.out.println("\nUpdated Single Room Availability: " + inventory.getAvailableRooms("Single"));
    }
}