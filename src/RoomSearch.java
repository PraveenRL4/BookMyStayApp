import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================================
 * DOMAIN MODEL - Room
 * =============================================================================
 */
class Room {
    private String type;
    private int beds;
    private int size;
    private double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayDetails(int availableCount) {
        System.out.println(type + " Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + availableCount + "\n");
    }
}

/**
 * =============================================================================
 * STATE HOLDER - RoomInventory
 * =============================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addInventory(String type, int count) {
        inventory.put(type, count);
    }

    /**
     * Returns a copy of the inventory map to ensure read-only access
     * and prevent accidental mutation of the original state.
     */
    public Map<String, Integer> getRoomAvailability() {
        return new HashMap<>(inventory);
    }
}

/**
 * =============================================================================
 * CLASS - RoomSearchService
 * =============================================================================
 * This class provides search functionality for guests to view available rooms.
 * It reads room availability from inventory and room details from Room objects.
 * No inventory mutation or booking logic is performed in this class.
 */
class RoomSearchService {

    /**
     * Displays available rooms along with their details and pricing.
     * This method performs read-only access to inventory and room data.
     * * @param inventory  centralized room inventory
     * @param singleRoom single room definition
     * @param doubleRoom double room definition
     * @param suiteRoom  suite room definition
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Room Search\n");

        // Check and display Single Room availability
        if (availability.getOrDefault("Single", 0) > 0) {
            singleRoom.displayDetails(availability.get("Single"));
        }

        // Check and display Double Room availability
        if (availability.getOrDefault("Double", 0) > 0) {
            doubleRoom.displayDetails(availability.get("Double"));
        }

        // Check and display Suite Room availability
        if (availability.getOrDefault("Suite", 0) > 0) {
            suiteRoom.displayDetails(availability.get("Suite"));
        }
    }
}

/**
 * =============================================================================
 * MAIN CLASS - UseCase4RoomSearch
 * =============================================================================
 * This class demonstrates how guests can view available rooms without
 * modifying inventory data. The system enforces read-only access by design.
 * * @version 4.0
 */
public class RoomSearch {

    public static void main(String[] args) {
        // 1. Setup Inventory (State)
        RoomInventory inventory = new RoomInventory();
        inventory.addInventory("Single", 5);
        inventory.addInventory("Double", 3);
        inventory.addInventory("Suite", 2);

        // 2. Setup Room Definitions (Domain Models)
        Room single = new Room("Single", 1, 250, 1500.0);
        Room doubleRm = new Room("Double", 2, 400, 2500.0);
        Room suite = new Room("Suite", 3, 750, 5000.0);

        // 3. Execute Search Service
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory, single, doubleRm, suite);
    }
}