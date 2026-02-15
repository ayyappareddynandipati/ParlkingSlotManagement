package com.parking.service.core;

import com.parking.exception.InvalidTicketException;
import com.parking.exception.VehicleNotFoundException;
import com.parking.model.*;
import com.parking.service.interfaces.IExitGateHandler;
import com.parking.service.interfaces.IParkingService;

/**
 * Exit Gate Handler for processing vehicle exits.
 * Single Responsibility - only handles exit gate operations.
 * Implements IExitGateHandler interface for Dependency Inversion.
 */
public class ExitGateHandler implements IExitGateHandler {
    
    private String gateId;
    private IParkingService parkingService;
    
    public ExitGateHandler(String gateId, IParkingService parkingService) {
        this.gateId = gateId;
        this.parkingService = parkingService;
    }
    
    /**
     * Process vehicle exit through this gate.
     * @throws InvalidTicketException if the ticket ID is invalid
     */
    public boolean processExit(String ticketId) {
        // Validate ticket first
        Ticket ticket = parkingService.getTicket(ticketId);
        
        if (ticket == null) {
            throw new InvalidTicketException(ticketId);
        }
        
        // Show gate header only for valid tickets
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf("║        EXIT GATE %s                    ║%n", gateId);
        System.out.println("╚═══════════════════════════════════════╝");
        
        // Process exit
        boolean success = parkingService.unparkVehicle(ticketId);
        
        if (success) {
            ticket.setExitGateId(gateId);
            System.out.println("✓ Vehicle exited successfully. Have a nice day!");
        }
        
        return success;
    }
    
    /**
     * Handle lost ticket scenario.
     * @throws VehicleNotFoundException if the vehicle is not found in parking records
     */
    public boolean processLostTicket(String vehicleNumber) {
        // Find ticket by vehicle number first
        Ticket ticket = parkingService.findTicketByVehicle(vehicleNumber);
        
        if (ticket == null) {
            throw new VehicleNotFoundException(vehicleNumber);
        }
        
        // Show header only for valid vehicle
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║        LOST TICKET PROCESSING         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        
        ticket.markAsLost();
        
        // Apply lost ticket penalty (double the fare)
        double fare = calculateFare(ticket) * 2;
        System.out.println("⚠ Lost ticket penalty applied: 2x parking fee");
        System.out.printf("Amount Due: Rs. %.2f%n", fare);
        
        // Process exit
        return parkingService.unparkVehicle(ticket.getTicketId());
    }
    
    public String getGateId() {
        return gateId;
    }
    
    private double calculateFare(Ticket ticket) {
        ParkingLot lot = ParkingLot.getInstance();
        long hours = ticket.getParkingDurationHours();
        double rate = lot.getHourlyRate(ticket.getVehicleType());
        return hours * rate;
    }
}
