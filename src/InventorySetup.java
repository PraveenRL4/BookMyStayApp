
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * CLASS – Room
 * Represents a hotel room with its characteristics.
 * ============================================================
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

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}


/**
 * ============================================================
 * CLASS – RoomInventory
 * Centralized inventory using HashMap
 * ============================================================
 */
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}


/**
 * ============================================================
 * MAIN CLASS – UseCase3InventorySetup
 * Demonstrates centralized inventory management
 * ============================================================
 */
public class InventorySetup {

    public static void main(String[] args) {

        Room single = new Room("Single Room", 1, 250, 1500.0);
        Room doubleRoom = new Room("Double Room", 2, 400, 2500.0);
        Room suite = new Room("Suite Room", 3, 750, 5000.0);

        RoomInventory inventory = new RoomInventory();

        System.out.println("Hotel Room Inventory Status\n");

        displayRoom(single, inventory);
        displayRoom(doubleRoom, inventory);
        displayRoom(suite, inventory);
    }

    private static void displayRoom(Room room, RoomInventory inventory) {

        int available = inventory.getRoomAvailability().get(room.getType());

        System.out.println(room.getType() + ":");
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Size: " + room.getSize() + " sqft");
        System.out.println("Price per night: " + room.getPrice());
        System.out.println("Available Rooms: " + available);
        System.out.println();
    }
}