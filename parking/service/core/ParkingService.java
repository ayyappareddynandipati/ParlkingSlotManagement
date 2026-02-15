
package com.parking.service.core;

import com.parking.exception.NoFloorsConfiguredException;
import com.parking.model.*;
import com.parking.service.interfaces.FeeCalculator;
import com.parking.service.interfaces.IParkingService;
import com.parking.service.interfaces.SlotAllocationStrategy;
import com.parking.service.strategy.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Parking Service for handling vehicle parking and unparking.
 * Follows Single Responsibility Principle - only handles parking operations.
 * Follows Dependency Inversion Principle - depends on abstractions (interfaces).
 * Implements IParkingService interface.
 */
public class ParkingService implements IParkingService {

    private ParkingLot parkingLot;
    private SlotAllocationStrategy parkingStrategy;  // Depends on interface
    private FeeCalculator feeCalculator;              // Depends on interface
    private AtomicInteger ticketCounter;
    
    /**
     * Default constructor with default strategies.
     */
    public ParkingService() {
        this.parkingLot = ParkingLot.getInstance();
        this.parkingStrategy = new NearestSlotStrategy();
        this.feeCalculator = new HourlyFeeCalculator();
        this.ticketCounter = new AtomicInteger(1);
    }
    
    /**
     * Constructor with custom strategies (Dependency Injection).
     */
    public ParkingService(SlotAllocationStrategy parkingStrategy, FeeCalculator feeCalculator) {
        this.parkingLot = ParkingLot.getInstance();
        this.parkingStrategy = parkingStrategy;
        this.feeCalculator = feeCalculator;
        this.ticketCounter = new AtomicInteger(1);
    }
    
    // Legacy constructor for backward compatibility
    public ParkingService(ArrayList<Floor> floorList) {
        this.parkingLot = ParkingLot.getInstance();
        // Add floors if not already added
        for (Floor floor : floorList) {
            if (parkingLot.getFloor(floor.getFloorNumber()) == null) {
                parkingLot.addFloor(floor);
            }
        }
        this.parkingStrategy = new NearestSlotStrategy();
        this.feeCalculator = new HourlyFeeCalculator();
        this.ticketCounter = new AtomicInteger(1);
    }
    
    /**
     * Set parking strategy (allows runtime strategy change).
     */
    public void setParkingStrategy(SlotAllocationStrategy strategy) {
        this.parkingStrategy = strategy;
    }
    
    /**
     * Set fee calculator (allows runtime strategy change).
     */
    public void setFeeCalculator(FeeCalculator calculator) {
        this.feeCalculator = calculator;
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Error: Vehicle cannot be null.");
            return null;
        }
        
        // Check if vehicle is already parked
        if (isVehicleParked(vehicle.getVehicleNumber())) {
            System.out.println("Error: Vehicle " + vehicle.getVehicleNumber() + " is already parked.");
            return null;
        }
        
        // Find available slot using strategy
        Slot slot = parkingStrategy.findAvailableSlot(parkingLot.getFloors(), vehicle.getVehicleType());
        
        if (slot == null) {
            System.out.println("Error: No available slots for " + vehicle.getVehicleType());
            return null;
        }
        
        // Park the vehicle
        if (!slot.park(vehicle)) {
            System.out.println("Error: Failed to park vehicle.");
            return null;
        }
        
        // Generate ticket
        String ticketId = generateTicketId(slot.getFloorNumber(), vehicle.getVehicleType());
        Ticket ticket = new Ticket(ticketId, vehicle.getVehicleNumber(), 
            vehicle.getVehicleType(), slot.getSlotId(), slot.getFloorNumber());
        
        parkingLot.addActiveTicket(ticket);
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         VEHICLE PARKED SUCCESSFULLY    ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  Ticket ID: %-27s║%n", ticketId);
        System.out.printf("║  Vehicle  : %-27s║%n", vehicle.getVehicleNumber());
        System.out.printf("║  Slot     : %-27s║%n", slot.getSlotId());
        System.out.printf("║  Floor    : %-27d║%n", slot.getFloorNumber());
        System.out.println("╚════════════════════════════════════════╝");
        
        return ticket;
    }
    
    /**
     * Legacy park method for backward compatibility.
     */
    public void parkVehicle(String vehicleNumber, int typeCode) {
        VehicleType type = VehicleType.fromCode(typeCode);
        if (type == null) {
            System.out.println("Error: Invalid vehicle type.");
            return;
        }
        
        Vehicle vehicle = VehicleFactory.createVehicle(vehicleNumber, "Unknown", type);
        parkVehicle(vehicle);
    }

    public boolean unparkVehicle(String ticketId) {
        Ticket ticket = parkingLot.getActiveTicket(ticketId);
        
        if (ticket == null) {
            System.out.println("Error: Invalid Ticket ID - " + ticketId);
            return false;
        }
        
        if (!ticket.isActive()) {
            System.out.println("Error: Ticket " + ticketId + " has already been used for exit.");
            return false;
        }
        
        // Calculate fare
        double fare = feeCalculator.calculateFee(ticket);
        
        // Close ticket
        ticket.closeTicket(fare);
        
        // Unpark the vehicle from slot
        Slot slot = parkingLot.findSlotById(ticket.getSlotId());
        if (slot != null) {
            slot.unpark();
        }
        
        // Archive the ticket
        parkingLot.archiveTicket(ticketId);
        
        // Print receipt
        System.out.println(ticket.generateReceipt());
        
        return true;
    }

    public boolean isVehicleParked(String vehicleNumber) {
        return parkingLot.isVehicleParked(vehicleNumber);
    }

    public Ticket getTicket(String ticketId) {
        return parkingLot.getActiveTicket(ticketId);
    }
    
    /**
     * Find ticket by vehicle number.
     */
    public Ticket findTicketByVehicle(String vehicleNumber) {
        return parkingLot.findTicketByVehicle(vehicleNumber);
    }
    
    /**
     * Get availability for a vehicle type.
     */
    public int getAvailableSlots(VehicleType type) {
        return parkingLot.getAvailableSlotsByType(type);
    }
    
    /**
     * Display current parking lot status.
     * @throws NoFloorsConfiguredException if no floors are configured
     */
    public void displayAvailability() {
        if (parkingLot.getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        System.out.println(parkingLot.getStatusSummary());
    }
    
    /**
     * Display all floor availability.
     * @throws NoFloorsConfiguredException if no floors are configured
     */
    public void displayFloorAvailability() {
        if (parkingLot.getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║               FLOOR-WISE AVAILABILITY                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        
        for (Floor floor : parkingLot.getFloors()) {
            System.out.printf("║  Floor %d: %-48s║%n", 
                floor.getFloorNumber(), 
                String.format("Total: %d | Available: %d | Occupied: %d",
                    floor.getTotalSlots(), floor.getAvailableSlots(), floor.getOccupiedSlots()));
                    
            for (VehicleType type : VehicleType.values()) {
                int available = floor.countAvailableSlotsByType(type);
                int total = floor.countSlotsByType(type);
                System.out.printf("║    %s: %d/%d %-42s║%n", 
                    type.getCode() + "W", available, total, "");
            }
        }
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
    
    private String generateTicketId(int floorNumber, VehicleType type) {
        return String.format("F%d-%dW-T%d", floorNumber, type.getCode(), ticketCounter.getAndIncrement());
    }
}
