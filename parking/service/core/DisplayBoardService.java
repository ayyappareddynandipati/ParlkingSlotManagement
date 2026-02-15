package com.parking.service.core;

import com.parking.exception.FloorNotFoundException;
import com.parking.exception.NoFloorsConfiguredException;
import com.parking.model.*;
import com.parking.service.interfaces.IDisplayBoardService;
import java.util.*;

/**
 * Display Board service for showing real-time parking availability.
 * Implements IDisplayBoardService interface for Dependency Inversion.
 */
public class DisplayBoardService implements IDisplayBoardService {
    
    private ParkingLot parkingLot;
    
    public DisplayBoardService() {
        this.parkingLot = ParkingLot.getInstance();
    }
    
    public void update(Floor floor) {
        if (floor == null) {
            System.out.println("Error: Floor is null");
            return;
        }
        
        System.out.println("\n┌────────────────────────────────────────────────────┐");
        System.out.printf("│           FLOOR %d AVAILABILITY                     │%n", floor.getFloorNumber());
        System.out.println("├────────────────────────────────────────────────────┤");
        
        for (VehicleType type : VehicleType.values()) {
            int available = floor.countAvailableSlotsByType(type);
            int total = floor.countSlotsByType(type);
            int occupied = floor.countOccupiedSlotsByType(type);
            
            String bar = generateProgressBar(occupied, total);
            
            System.out.printf("│  %s: %s %d/%d │%n", 
                String.format("%-12s", type.getDescription()), bar, available, total);
        }
        
        System.out.println("└────────────────────────────────────────────────────┘");
    }
    
    public void showAllFloorsAvailability() {
        List<Floor> floors = parkingLot.getFloors();
        
        if (floors.isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     PARKING LOT DISPLAY BOARD                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        
        // Header
        System.out.println("║  Floor  │   2W (Bikes)   │   4W (Cars)    │   6W (Bus)     │  Total  ║");
        System.out.println("╠═════════╪════════════════╪════════════════╪════════════════╪═════════╣");
        
        int totalAvailable = 0;
        int totalCapacity = 0;
        
        for (Floor floor : floors) {
            int tw = floor.countAvailableSlotsByType(VehicleType.TWO_WHEELER);
            int twT = floor.countSlotsByType(VehicleType.TWO_WHEELER);
            int fw = floor.countAvailableSlotsByType(VehicleType.FOUR_WHEELER);
            int fwT = floor.countSlotsByType(VehicleType.FOUR_WHEELER);
            int sw = floor.countAvailableSlotsByType(VehicleType.SIX_WHEELER);
            int swT = floor.countSlotsByType(VehicleType.SIX_WHEELER);
            
            int floorAvailable = tw + fw + sw;
            int floorTotal = twT + fwT + swT;
            
            totalAvailable += floorAvailable;
            totalCapacity += floorTotal;
            
            String status = floor.isOperational() ? "" : " [CLOSED]";
            
            System.out.printf("║    %d%s   │    %3d/%-3d     │    %3d/%-3d     │    %3d/%-3d     │  %3d/%-3d ║%n",
                floor.getFloorNumber(), status, tw, twT, fw, fwT, sw, swT, floorAvailable, floorTotal);
        }
        
        System.out.println("╠═════════╧════════════════╧════════════════╧════════════════╧═════════╣");
        System.out.printf("║  TOTAL AVAILABLE: %-18d TOTAL CAPACITY: %-14d ║%n", 
            totalAvailable, totalCapacity);
        
        // Status indicator
        double occupancy = totalCapacity > 0 ? ((double)(totalCapacity - totalAvailable) / totalCapacity) * 100 : 0;
        String statusIndicator = getStatusIndicator(occupancy);
        System.out.printf("║  OCCUPANCY: %.1f%% %s                                             ║%n", 
            occupancy, statusIndicator);
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }
    
    public Map<Integer, String> getAvailabilitySummary() {
        Map<Integer, String> summary = new LinkedHashMap<>();
        
        for (Floor floor : parkingLot.getFloors()) {
            summary.put(floor.getFloorNumber(), floor.getAvailabilitySummary());
        }
        
        return summary;
    }
    
    /**
     * Show compact availability view.
     * @throws NoFloorsConfiguredException if no floors are configured
     */
    public void showCompactView() {
        if (parkingLot.getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       PARKING AVAILABILITY          │");
        System.out.println("├─────────────────────────────────────┤");
        
        for (VehicleType type : VehicleType.values()) {
            int available = parkingLot.getAvailableSlotsByType(type);
            String status = available > 0 ? "✓ AVAILABLE" : "✗ FULL     ";
            System.out.printf("│  %s: %3d slots %-11s │%n", 
                type.getCode() + "W", available, status);
        }
        
        System.out.println("└─────────────────────────────────────┘");
    }
    
    /**
     * Show detailed slot map for a floor.
     * @throws NoFloorsConfiguredException if no floors are configured
     * @throws FloorNotFoundException if the specified floor does not exist
     */
    public void showSlotMap(int floorNumber) {
        if (parkingLot.getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        Floor floor = parkingLot.getFloor(floorNumber);
        
        if (floor == null) {
            throw new FloorNotFoundException(floorNumber, getAvailableFloorNumbers());
        }
        
        System.out.println("\n┌───────────────────────────────────────────────────────────┐");
        System.out.printf("│                    FLOOR %d SLOT MAP                       │%n", floorNumber);
        System.out.println("├───────────────────────────────────────────────────────────┤");
        
        for (VehicleType type : VehicleType.values()) {
            System.out.printf("│  %s Slots:                                              │%n", type.getCode() + "W");
            System.out.print("│  ");
            
            int count = 0;
            for (Slot slot : floor.getSlotList()) {
                if (slot.getVehicleType() == type) {
                    String symbol = slot.isOccupied() ? "[X]" : "[ ]";
                    System.out.print(symbol + " ");
                    count++;
                    if (count % 10 == 0) {
                        System.out.println("│");
                        System.out.print("│  ");
                    }
                }
            }
            
            // Pad remaining space
            int remaining = 10 - (count % 10);
            if (count % 10 != 0) {
                for (int i = 0; i < remaining; i++) {
                    System.out.print("    ");
                }
            }
            System.out.println("│");
        }
        
        System.out.println("│  Legend: [ ] = Available, [X] = Occupied                 │");
        System.out.println("└───────────────────────────────────────────────────────────┘");
    }
    
    private String generateProgressBar(int occupied, int total) {
        if (total == 0) return "[          ]";
        
        int filledBlocks = (int) ((double) occupied / total * 10);
        StringBuilder bar = new StringBuilder("[");
        
        for (int i = 0; i < 10; i++) {
            if (i < filledBlocks) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        
        return bar.toString();
    }
    
    private String getStatusIndicator(double occupancy) {
        if (occupancy >= 90) {
            return "🔴 ALMOST FULL";
        } else if (occupancy >= 70) {
            return "🟡 FILLING UP";
        } else if (occupancy >= 50) {
            return "🟢 MODERATE";
        } else {
            return "🟢 AVAILABLE";
        }
    }
    
    private String getAvailableFloorNumbers() {
        StringBuilder sb = new StringBuilder();
        for (Floor floor : parkingLot.getFloors()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(floor.getFloorNumber());
        }
        return sb.toString();
    }
}
