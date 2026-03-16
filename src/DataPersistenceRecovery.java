import java.io.*;
import java.util.*;

/**
 * ===================================================================
 * SUPPORTING CLASS - RoomInventory
 * ===================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void setRoomCount(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }

    public void initializeDefaults() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count));
    }
}

/**
 * ===================================================================
 * CLASS - FilePersistenceService
 * ===================================================================
 */
class FilePersistenceService {

    /**
     * Saves room inventory state to a file.
     * Format: roomType=availableCount
     */
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     */
    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            inventory.initializeDefaults();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    inventory.setRoomCount(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading inventory. Starting fresh.");
            inventory.initializeDefaults();
        }
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase12DataPersistenceRecovery
 * ===================================================================
 */
public class DataPersistenceRecovery {

    public static void main(String[] args) {
        System.out.println("System Recovery");

        // 1. Initialize components
        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();
        String storageFile = "inventory_data.txt";

        // 2. Simulate System Startup: Load data from file
        persistenceService.loadInventory(inventory, storageFile);

        // 3. Display restored state
        inventory.showInventory();

        // 4. Simulate System Shutdown: Save data to file
        persistenceService.saveInventory(inventory, storageFile);
    }
}