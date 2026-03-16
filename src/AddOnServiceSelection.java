import java.util.*;

/**
 * ===================================================================
 * CLASS - Service (Representing an individual optional offering)
 * ===================================================================
 */
class Service {
    private String serviceName;
    private double cost;

    /**
     * Creates a new add-on service.
     * @param serviceName name of the service
     * @param cost cost of the service
     */
    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    /** @return service name */
    public String getServiceName() { return serviceName; }

    /** @return service cost */
    public double getCost() { return cost; }
}

/**
 * ===================================================================
 * CLASS - AddOnServiceManager
 * ===================================================================
 */
class AddOnServiceManager {
    /** Maps reservation ID to selected services.
     * Key -> Reservation ID, Value -> List of selected services */
    private Map<String, List<Service>> servicesByReservation;

    /** Initializes the service manager. */
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    /**
     * Attaches a service to a reservation.
     * @param reservationId confirmed reservation ID
     * @param service add-on service
     */
    public void addService(String reservationId, Service service) {
        servicesByReservation
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    /**
     * Calculates total add-on cost for a reservation.
     * @param reservationId reservation ID
     * @return total service cost
     */
    public double calculateTotalServiceCost(String reservationId) {
        List<Service> services = servicesByReservation.get(reservationId);
        if (services == null) return 0.0;

        double total = 0.0;
        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }
}

/**
 * ===================================================================
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * ===================================================================
 */
public class AddOnServiceSelection {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("Add-On Service Selection");

        // Initialize Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Target Reservation ID (Generated from Use Case 6)
        String reservationId = "Single-1";

        // Create Add-On Services
        Service breakfast = new Service("Breakfast", 500.0);
        Service spa = new Service("Spa", 1000.0);

        // Attach services to the reservation
        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, spa);

        // Output results
        System.out.println("Reservation ID: " + reservationId);
        double totalCost = serviceManager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}