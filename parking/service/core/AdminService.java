
package com.parking.service.core;

import com.parking.exception.NoFloorsConfiguredException;
import com.parking.model.*;
import com.parking.service.interfaces.IAdminService;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Service for managing parking lot configuration.
 * Follows Single Responsibility Principle - only handles admin operations.
 * Implements IAdminService interface for Dependency Inversion.
 */
public class AdminService implements IAdminService {

    private ParkingLot parkingLot;

    public AdminService() {
        this.parkingLot = ParkingLot.getInstance();
    }
    
    // Legacy constructor for backward compatibility
    public AdminService(ArrayList<Floor> floorList) {
        this.parkingLot = ParkingLot.getInstance();
        // Add existing floors to parking lot
        for (Floor floor : floorList) {
            parkingLot.addFloor(floor);
        }
    }

    public void addFloors(int count, int twoW, int fourW, int sixW) {
        List<Floor> existingFloors = parkingLot.getFloors();
        
        int nextFloorNumber = 1;
        if (!existingFloors.isEmpty()) {
            nextFloorNumber = existingFloors.get(existingFloors.size() - 1).getFloorNumber() + 1;
        }

        for (int i = 0; i < count; i++) {
            Floor floor = new Floor(nextFloorNumber);

            createSlots(floor, twoW, VehicleType.TWO_WHEELER);
            createSlots(floor, fourW, VehicleType.FOUR_WHEELER);
            createSlots(floor, sixW, VehicleType.SIX_WHEELER);

            parkingLot.addFloor(floor);
            System.out.println("Floor " + nextFloorNumber + " added successfully with " + 
                (twoW + fourW + sixW) + " slots.");
            nextFloorNumber++;
        }
    }

    private void createSlots(Floor floor, int count, VehicleType type) {
        for (int i = 1; i <= count; i++) {
            String slotId = "F" + floor.getFloorNumber() + "-" + type.getCode() + "W-S" + i;
            Slot slot = new Slot(slotId, type, floor.getFloorNumber(), i);
            floor.addSlot(slot);
        }
    }

    public boolean deleteFloor(int floorNumber) {
        Floor target = parkingLot.getFloor(floorNumber);

        if (target == null) {
            System.out.println("Error: Floor " + floorNumber + " does not exist.");
            return false;
        }

        if (target.hasOccupiedSlots()) {
            System.out.println("Error: Cannot delete floor " + floorNumber + " - has occupied slots.");
            return false;
        }

        boolean removed = parkingLot.removeFloor(floorNumber);
        if (removed) {
            System.out.println("Floor " + floorNumber + " deleted successfully.");
        }
        return removed;
    }

    public boolean convertSlots(int floorNumber, VehicleType fromType, VehicleType toType, int convertCount) {
        Floor floor = parkingLot.getFloor(floorNumber);

        if (floor == null) {
            System.out.println("Error: Floor " + floorNumber + " does not exist.");
            return false;
        }

        if (fromType == null || toType == null) {
            System.out.println("Error: Invalid vehicle type.");
            return false;
        }

        int available = floor.countAvailableSlotsByType(fromType);

        if (convertCount > available) {
            System.out.println("Error: Only " + available + " available " + fromType + " slots. Cannot convert " + convertCount + ".");
            return false;
        }

        int changed = 0;
        for (Slot s : floor.getSlotList()) {
            if (s.getVehicleType() == fromType && changed < convertCount && !s.isOccupied()) {
                s.setVehicleType(toType);
                changed++;
            }
        }

        System.out.println(changed + " slots converted from " + fromType.getCode() + "W to " + toType.getCode() + "W successfully.");
        return true;
    }
    
    // Overloaded method for backward compatibility
    public void convertSlots(int floorNumber, int fromTypeCode, int toTypeCode, int convertCount) {
        VehicleType fromType = VehicleType.fromCode(fromTypeCode);
        VehicleType toType = VehicleType.fromCode(toTypeCode);
        convertSlots(floorNumber, fromType, toType, convertCount);
    }

    public void setHourlyRate(VehicleType vehicleType, double rate) {
        if (vehicleType == null) {
            System.out.println("Error: Invalid vehicle type.");
            return;
        }
        if (rate < 0) {
            System.out.println("Error: Rate cannot be negative.");
            return;
        }
        parkingLot.setHourlyRate(vehicleType, rate);
        System.out.println("Hourly rate for " + vehicleType + " set to Rs. " + rate);
    }

    public int getTotalCapacity() {
        return parkingLot.getTotalCapacity();
    }

    public int getAvailableSlots() {
        return parkingLot.getTotalAvailableSlots();
    }
    
    /**
     * Display parking lot status.
     * @throws NoFloorsConfiguredException if no floors are configured
     */
    public void displayStatus() {
        if (parkingLot.getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        System.out.println(parkingLot.getStatusSummary());
    }
    
    /**
     * Configure parking lot name and address.
     */
    public void configureParkingLot(String name, String address) {
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     PARKING LOT CONFIGURED            ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.printf("║  Name   : %-28s║%n", name);
        System.out.printf("║  Address: %-28s║%n", address);
        System.out.println("╚═══════════════════════════════════════╝");
    }
    
    /**
     * Display current hourly rates.
     */
    public void displayRates() {
        System.out.println("\nCurrent Hourly Rates:");
        System.out.println("─────────────────────");
        for (VehicleType type : VehicleType.values()) {
            System.out.printf("  %s: Rs. %.2f/hour%n", type, parkingLot.getHourlyRate(type));
        }
        System.out.println();
    }
    
    /**
     * Add slots to an existing floor.
     */
    public boolean addSlotsToFloor(int floorNumber, int count, VehicleType type) {
        Floor floor = parkingLot.getFloor(floorNumber);
        
        if (floor == null) {
            System.out.println("Error: Floor " + floorNumber + " does not exist.");
            return false;
        }
        
        if (type == null) {
            System.out.println("Error: Invalid vehicle type.");
            return false;
        }
        
        // Find the next slot number for this type
        int maxSlotNum = 0;
        for (Slot s : floor.getSlotList()) {
            if (s.getVehicleType() == type) {
                maxSlotNum++;
            }
        }
        
        for (int i = 1; i <= count; i++) {
            String slotId = "F" + floorNumber + "-" + type.getCode() + "W-S" + (maxSlotNum + i);
            Slot slot = new Slot(slotId, type, floorNumber, maxSlotNum + i);
            floor.addSlot(slot);
        }
        
        System.out.println(count + " slots of type " + type.getCode() + "W added to Floor " + floorNumber + " successfully.");
        return true;
    }
    
    /**
     * Delete slots from a floor (only empty slots can be deleted).
     */
    public boolean deleteSlotsFromFloor(int floorNumber, int count, VehicleType type) {
        Floor floor = parkingLot.getFloor(floorNumber);
        
        if (floor == null) {
            System.out.println("Error: Floor " + floorNumber + " does not exist.");
            return false;
        }
        
        if (type == null) {
            System.out.println("Error: Invalid vehicle type.");
            return false;
        }
        
        int availableToDelete = floor.countAvailableSlotsByType(type);
        
        if (count > availableToDelete) {
            System.out.println("Error: Only " + availableToDelete + " empty " + type.getCode() + "W slots available to delete.");
            return false;
        }
        
        int deleted = 0;
        List<Slot> toRemove = new ArrayList<>();
        
        for (Slot s : floor.getSlotList()) {
            if (s.getVehicleType() == type && !s.isOccupied() && deleted < count) {
                toRemove.add(s);
                deleted++;
            }
        }
        
        floor.getSlotList().removeAll(toRemove);
        
        System.out.println(deleted + " slots of type " + type.getCode() + "W deleted from Floor " + floorNumber + " successfully.");
        return true;
    }
    
    /**
     * Edit slot properties (mark as reserved).
     */
    public boolean editSlot(String slotId, boolean reserved) {
        Slot slot = parkingLot.findSlotById(slotId);
        
        if (slot == null) {
            System.out.println("Error: Slot " + slotId + " not found.");
            return false;
        }
        
        slot.setReserved(reserved);
        
        System.out.println("Slot " + slotId + " updated successfully.");
        System.out.println("  Reserved: " + (reserved ? "Yes" : "No"));
        
        return true;
    }
    
    /**
     * Display all floors with visual slot representation.
     * @throws NoFloorsConfiguredException if no floors are configured
     */
    public void displayAllFloorsWithSlots() {
        List<Floor> floors = parkingLot.getFloors();
        
        if (floors.isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ALL FLOORS - SLOT STATUS                              ║");
        System.out.println("║                  [ ] = Available    [X] = Occupied    [R] = Reserved         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        for (Floor floor : floors) {
            displayFloorSlots(floor);
        }
    }
    
    /**
     * Display a single floor with visual slot representation.
     */
    public void displayFloorSlots(int floorNumber) {
        Floor floor = parkingLot.getFloor(floorNumber);
        
        if (floor == null) {
            System.out.println("Error: Floor " + floorNumber + " does not exist.");
            return;
        }
        
        displayFloorSlots(floor);
    }
    
    private void displayFloorSlots(Floor floor) {
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│  FLOOR %d                                                                     │%n", floor.getFloorNumber());
        System.out.printf("│  Total: %d slots | Available: %d | Occupied: %d                               │%n", 
            floor.getTotalSlots(), floor.getAvailableSlots(), floor.getOccupiedSlots());
        System.out.println("├──────────────────────────────────────────────────────────────────────────────┤");
        
        for (VehicleType type : VehicleType.values()) {
            List<Slot> slots = new ArrayList<>();
            for (Slot s : floor.getSlotList()) {
                if (s.getVehicleType() == type) {
                    slots.add(s);
                }
            }
            
            if (slots.isEmpty()) {
                continue;
            }
            
            int available = floor.countAvailableSlotsByType(type);
            int total = floor.countSlotsByType(type);
            //int occupied = floor.countOccupiedSlotsByType(type);
            
            System.out.printf("│  %s (%d/%d available)                                          │%n", 
                type.getCode() + "W - " + type.getDescription(), available, total);
            System.out.print("│  ");
            
            int count = 0;
            for (Slot slot : slots) {
                String symbol = getSlotSymbol(slot);
                System.out.print(symbol + " ");
                count++;
                
                if (count % 15 == 0 && count < slots.size()) {
                    System.out.println("│");
                    System.out.print("│  ");
                }
            }
            
            // Pad remaining space
            int remaining = 15 - (count % 15);
            if (count % 15 != 0) {
                for (int i = 0; i < remaining; i++) {
                    System.out.print("    ");
                }
            }
            System.out.println("│");
        }
        
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
    }
    
    private String getSlotSymbol(Slot slot) {
        if (slot.isOccupied()) {
            return "[X]";
        } else if (slot.isReserved()) {
            return "[R]";
        } else {
            return "[ ]";
        }
    }
    
    /**
     * Display detailed slot information.
     */
    public void displaySlotDetails(String slotId) {
        Slot slot = parkingLot.findSlotById(slotId);
        
        if (slot == null) {
            System.out.println("Error: Slot " + slotId + " not found.");
            return;
        }
        
        System.out.println();
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│         SLOT DETAILS                │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.printf("│  Slot ID    : %-21s │%n", slot.getSlotId());
        System.out.printf("│  Floor      : %-21d │%n", slot.getFloorNumber());
        System.out.printf("│  Type       : %-21s │%n", slot.getVehicleType().getCode() + "W");
        System.out.printf("│  Status     : %-21s │%n", slot.isOccupied() ? "OCCUPIED" : "AVAILABLE");
        System.out.printf("│  Reserved   : %-21s │%n", slot.isReserved() ? "Yes" : "No");
        if (slot.isOccupied() && slot.getParkedVehicle() != null) {
            System.out.printf("│  Vehicle    : %-21s │%n", slot.getParkedVehicle().getVehicleNumber());
        }
        System.out.println("└─────────────────────────────────────┘");
    }
    
    /**
     * Get list of all floors.
     */
    public List<Floor> getAllFloors() {
        return parkingLot.getFloors();
    }
}
