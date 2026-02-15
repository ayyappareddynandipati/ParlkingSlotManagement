

import com.parking.model.VehicleType;

/**
 * Interface for Admin Service operations.
 * Dependency Inversion: Controllers depend on this abstraction, not concrete implementations.
 */
public interface IAdminService {
    
    /**
     * Add floors to the parking lot.
     * @param count Number of floors to add
     * @param twoW Number of two-wheeler slots per floor
     * @param fourW Number of four-wheeler slots per floor
     * @param sixW Number of six-wheeler slots per floor
     */
    void addFloors(int count, int twoW, int fourW, int sixW);
    
    /**
     * Delete a floor from the parking lot.
     * @param floorNumber The floor number to delete
     * @return true if deletion was successful
     */
    boolean deleteFloor(int floorNumber);
    
    /**
     * Convert slots from one vehicle type to another.
     * @param floorNumber The floor number
     * @param fromType Source vehicle type
     * @param toType Target vehicle type
     * @param convertCount Number of slots to convert
     * @return true if conversion was successful
     */
    boolean convertSlots(int floorNumber, VehicleType fromType, VehicleType toType, int convertCount);
    
    /**
     * Set hourly rate for a vehicle type.
     * @param vehicleType The vehicle type
     * @param rate The hourly rate
     */
    void setHourlyRate(VehicleType vehicleType, double rate);
    
    /**
     * Get total capacity of the parking lot.
     * @return Total number of slots
     */
    int getTotalCapacity();
    
    /**
     * Get available slots count.
     * @return Number of available slots
     */
    int getAvailableSlots();
    
    /**
     * Display parking lot status.
     */
    void displayStatus();
    
    /**
     * Configure parking lot name and address.
     * @param name Parking lot name
     * @param address Parking lot address
     */
    void configureParkingLot(String name, String address);
    
    /**
     * Display current hourly rates.
     */
    void displayRates();
    
    /**
     * Add slots to an existing floor.
     * @param floorNumber The floor number
     * @param count Number of slots to add
     * @param type Vehicle type for new slots
     * @return true if slots were added successfully
     */
    boolean addSlotsToFloor(int floorNumber, int count, VehicleType type);
}
