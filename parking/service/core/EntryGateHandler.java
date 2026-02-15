package com.parking.service.core;

import com.parking.model.*;
import com.parking.service.interfaces.IEntryGateHandler;
import com.parking.service.interfaces.IDisplayBoardService;
import com.parking.service.interfaces.IParkingService;

/**
 * Entry Gate Handler for processing vehicle entries.
 * Single Responsibility - only handles entry gate operations.
 * Implements IEntryGateHandler interface for Dependency Inversion.
 */
public class EntryGateHandler implements IEntryGateHandler {
    
    private String gateId;
    private IParkingService parkingService;
    private IDisplayBoardService displayBoard;
    
    public EntryGateHandler(String gateId, IParkingService parkingService) {
        this.gateId = gateId;
        this.parkingService = parkingService;
        this.displayBoard = new DisplayBoardService();
    }
    
    /**
     * Constructor with custom display board (Dependency Injection).
     */
    public EntryGateHandler(String gateId, IParkingService parkingService, IDisplayBoardService displayBoard) {
        this.gateId = gateId;
        this.parkingService = parkingService;
        this.displayBoard = displayBoard;
    }
    
    /**
     * Process vehicle entry through this gate.
     */
    public Ticket processEntry(Vehicle vehicle) {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf("║       ENTRY GATE %s                    ║%n", gateId);
        System.out.println("╠═══════════════════════════════════════╣");
        
        // Check availability first
        ParkingLot lot = ParkingLot.getInstance();
        int available = lot.getAvailableSlotsByType(vehicle.getVehicleType());
        
        if (available == 0) {
            System.out.println("║  ⚠ SORRY! No slots available for      ║");
            System.out.printf("║    %s                               ║%n", vehicle.getVehicleType());
            System.out.println("╚═══════════════════════════════════════╝");
            return null;
        }
        
        System.out.printf("║  Vehicle: %-27s ║%n", vehicle.getVehicleNumber());
        System.out.printf("║  Type   : %-27s ║%n", vehicle.getVehicleType());
        System.out.println("╚═══════════════════════════════════════╝");
        
        // Park the vehicle
        Ticket ticket = parkingService.parkVehicle(vehicle);
        
        if (ticket != null) {
            ticket.setEntryGateId(gateId);
        }
        
        return ticket;
    }
    
    /**
     * Display current availability at this gate.
     */
    public void displayAvailability() {
        displayBoard.showCompactView();
    }
    
    /**
     * Check if parking is available for a vehicle type.
     */
    public boolean isSpaceAvailable(VehicleType type) {
        ParkingLot lot = ParkingLot.getInstance();
        return lot.getAvailableSlotsByType(type) > 0;
    }
    
    public String getGateId() {
        return gateId;
    }
}
