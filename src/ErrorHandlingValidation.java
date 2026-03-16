import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

/**
 * ===================================================================
 * CLASS - InvalidBookingException
 * ===================================================================
 */
class InvalidBookingException extends Exception {
    /**
     * Creates an exception with a descriptive error message.
     * @param message error description
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * ===================================================================
 * SUPPORTING CLASS - RoomInventory
 * ===================================================================
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        // Initializing with some sample data
        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Suite", 2);
    }

    public boolean isValidType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

/**
 * ===================================================================
 * CLASS - ReservationValidator
 * ===================================================================
 */
class ReservationValidator {
    /**
     * Validates booking input provided by the user.
     * @throws InvalidBookingException if validation fails
     */
    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        // 1. Validate Guest Name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // 2. Validate Room Type (Case Sensitive Check)
        if (!inventory.isValidType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ===================================================================
 */
public class ErrorHandlingValidation {

    public static void main(String[] args) {
        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        try {
            // Accept User Input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Perform Validation
            validator.validate(guestName, roomType, inventory);

            // If it reaches here, validation passed
            System.out.println("Validation successful for " + guestName);

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            // Ensure resources are closed
            scanner.close();
        }
    }
}