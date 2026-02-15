
package com.parking.model;

/**
 * Represents a parking slot in the parking lot.
 * Each slot can accommodate a specific vehicle type.
 */
public class Slot {

    private String slotId;
    private VehicleType vehicleType;
    private boolean occupied;
    private Vehicle parkedVehicle;
    private int floorNumber;
    private int slotNumber;
    private boolean isReserved;

    public Slot(String slotId, VehicleType vehicleType, int floorNumber, int slotNumber) {
        this.slotId = slotId;
        this.vehicleType = vehicleType;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.occupied = false;
        this.parkedVehicle = null;
        this.isReserved = false;
    }

    // Legacy constructor for backward compatibility
    public Slot(String slotId, VehicleType vehicleType) {
        this(slotId, vehicleType, 0, 0);
    }

    public String getSlotId() { 
        return slotId; 
    }
    
    public VehicleType getVehicleType() { 
        return vehicleType; 
    }
    
    public boolean isOccupied() { 
        return occupied; 
    }
    
    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
    
    public int getFloorNumber() {
        return floorNumber;
    }
    
    public int getSlotNumber() {
        return slotNumber;
    }
    
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * Park a vehicle in this slot.
     * @param vehicle The vehicle to park
     * @return true if parked successfully, false otherwise
     */
    public boolean park(Vehicle vehicle) {
        if (occupied || vehicle == null) {
            return false;
        }
        if (vehicle.getVehicleType() != this.vehicleType) {
            return false;
        }
        this.parkedVehicle = vehicle;
        this.occupied = true;
        return true;
    }
    
    /**
     * Legacy park method for backward compatibility.
     */
    public void park() { 
        occupied = true; 
    }
    
    /**
     * Unpark the vehicle from this slot.
     * @return The unparked vehicle, or null if slot was empty
     */
    public Vehicle unparkVehicle() {
        if (!occupied) {
            return null;
        }
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        this.occupied = false;
        return vehicle;
    }
    
    /**
     * Legacy unpark method for backward compatibility.
     */
    public void unpark() { 
        occupied = false;
        parkedVehicle = null;
    }

    public void setVehicleType(VehicleType type) {
        if (!occupied) {
            this.vehicleType = type;
        }
    }
    
    public void setReserved(boolean reserved) {
        this.isReserved = reserved;
    }
    
    /**
     * Check if slot is available for parking.
     * @return true if slot is available
     */
    public boolean isAvailable() {
        return !occupied && !isReserved;
    }
    
    @Override
    public String toString() {
        String status = occupied ? "OCCUPIED" : "AVAILABLE";
        return String.format("Slot[%s | %s | %s]", slotId, vehicleType, status);
    }
}
