
package com.parking.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a floor in the parking lot.
 * Each floor contains multiple parking slots.
 */
public class Floor {

    private int floorNumber;
    private String floorName;
    private ArrayList<Slot> slotList;
    private boolean isOperational;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.floorName = "Floor " + floorNumber;
        this.slotList = new ArrayList<>();
        this.isOperational = true;
    }
    
    public Floor(int floorNumber, String floorName) {
        this.floorNumber = floorNumber;
        this.floorName = floorName;
        this.slotList = new ArrayList<>();
        this.isOperational = true;
    }

    public int getFloorNumber() { 
        return floorNumber; 
    }
    
    public String getFloorName() {
        return floorName;
    }
    
    public ArrayList<Slot> getSlotList() { 
        return slotList; 
    }
    
    public boolean isOperational() {
        return isOperational;
    }
    
    public void setOperational(boolean operational) {
        this.isOperational = operational;
    }

    public void addSlot(Slot slot) {
        slotList.add(slot);
    }
    
    public boolean removeSlot(String slotId) {
        return slotList.removeIf(slot -> slot.getSlotId().equals(slotId) && !slot.isOccupied());
    }

    /**
     * Count total slots of a specific vehicle type.
     */
    public int countSlotsByType(VehicleType type) {
        int count = 0;
        for (Slot s : slotList) {
            if (s.getVehicleType() == type) count++;
        }
        return count;
    }
    
    /**
     * Count available slots of a specific vehicle type.
     */
    public int countAvailableSlotsByType(VehicleType type) {
        int count = 0;
        for (Slot s : slotList) {
            if (s.getVehicleType() == type && s.isAvailable()) count++;
        }
        return count;
    }
    
    /**
     * Count occupied slots of a specific vehicle type.
     */
    public int countOccupiedSlotsByType(VehicleType type) {
        int count = 0;
        for (Slot s : slotList) {
            if (s.getVehicleType() == type && s.isOccupied()) count++;
        }
        return count;
    }
    
    /**
     * Get total number of slots on this floor.
     */
    public int getTotalSlots() {
        return slotList.size();
    }
    
    /**
     * Get total available slots on this floor.
     */
    public int getAvailableSlots() {
        int count = 0;
        for (Slot s : slotList) {
            if (s.isAvailable()) count++;
        }
        return count;
    }
    
    /**
     * Get total occupied slots on this floor.
     */
    public int getOccupiedSlots() {
        int count = 0;
        for (Slot s : slotList) {
            if (s.isOccupied()) count++;
        }
        return count;
    }
    
    /**
     * Find first available slot for a vehicle type.
     */
    public Slot findAvailableSlot(VehicleType type) {
        for (Slot s : slotList) {
            if (s.getVehicleType() == type && s.isAvailable()) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Get all available slots of a specific type.
     */
    public List<Slot> getAvailableSlotsByType(VehicleType type) {
        return slotList.stream()
            .filter(s -> s.getVehicleType() == type && s.isAvailable())
            .collect(Collectors.toList());
    }
    
    /**
     * Find a slot by its ID.
     */
    public Slot findSlotById(String slotId) {
        for (Slot s : slotList) {
            if (s.getSlotId().equals(slotId)) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Check if floor has any occupied slots.
     */
    public boolean hasOccupiedSlots() {
        for (Slot s : slotList) {
            if (s.isOccupied()) return true;
        }
        return false;
    }
    
    /**
     * Get availability summary for display board.
     */
    public String getAvailabilitySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Floor %d: ", floorNumber));
        
        for (VehicleType type : VehicleType.values()) {
            int available = countAvailableSlotsByType(type);
            int total = countSlotsByType(type);
            sb.append(String.format("%s: %d/%d  ", type.getCode() + "W", available, total));
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Floor[%d | Total: %d | Available: %d | Occupied: %d]", 
            floorNumber, getTotalSlots(), getAvailableSlots(), getOccupiedSlots());
    }
}
