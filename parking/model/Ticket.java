
package com.parking.model;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Represents a parking ticket issued when a vehicle is parked.
 * Contains all information needed for tracking and billing.
 */
public class Ticket {

    public enum TicketStatus {
        ACTIVE, PAID, EXITED, LOST
    }

    private String ticketId;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String slotId;
    private int floorNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fare;
    private TicketStatus status;
    private String entryGateId;
    private String exitGateId;

    public Ticket(String ticketId, String vehicleNumber, VehicleType vehicleType, String slotId, int floorNumber) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotId = slotId;
        this.floorNumber = floorNumber;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }
    
    // Legacy constructor for backward compatibility
    public Ticket(String ticketId, String vehicleNumber, String slotId) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.slotId = slotId;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    // Getters
    public String getTicketId() { 
        return ticketId; 
    }
    
    public String getVehicleNumber() { 
        return vehicleNumber; 
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public String getSlotId() { 
        return slotId; 
    }
    
    public int getFloorNumber() {
        return floorNumber;
    }
    
    public LocalDateTime getEntryTime() { 
        return entryTime; 
    }
    
    public LocalDateTime getExitTime() { 
        return exitTime; 
    }
    
    public double getFare() { 
        return fare; 
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public String getEntryGateId() {
        return entryGateId;
    }
    
    public String getExitGateId() {
        return exitGateId;
    }
    
    // Setters
    public void setEntryGateId(String entryGateId) {
        this.entryGateId = entryGateId;
    }
    
    public void setExitGateId(String exitGateId) {
        this.exitGateId = exitGateId;
    }

    /**
     * Close the ticket when vehicle exits.
     * @param fare The calculated fare
     */
    public void closeTicket(double fare) {
        this.exitTime = LocalDateTime.now();
        this.fare = fare;
        this.status = TicketStatus.EXITED;
    }
    
    /**
     * Mark ticket as lost (higher fare applies).
     */
    public void markAsLost() {
        this.status = TicketStatus.LOST;
    }
    
    /**
     * Check if ticket is still active.
     */
    public boolean isActive() {
        return status == TicketStatus.ACTIVE || status == TicketStatus.PAID;
    }
    
    /**
     * Get parking duration in hours.
     */
    public long getParkingDurationHours() {
        LocalDateTime end = exitTime != null ? exitTime : LocalDateTime.now();
        Duration duration = Duration.between(entryTime, end);
        long hours = duration.toHours();
        // Minimum 1 hour, round up any partial hour
        if (duration.toMinutes() % 60 > 0) {
            hours++;
        }
        return Math.max(1, hours);
    }
    
    /**
     * Get parking duration in minutes.
     */
    public long getParkingDurationMinutes() {
        LocalDateTime end = exitTime != null ? exitTime : LocalDateTime.now();
        Duration duration = Duration.between(entryTime, end);
        return duration.toMinutes();
    }
    
    /**
     * Generate a detailed receipt string.
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========================================\n");
        sb.append("          PARKING RECEIPT\n");
        sb.append("========================================\n");
        sb.append(String.format("Ticket ID     : %s\n", ticketId));
        sb.append(String.format("Vehicle No    : %s\n", vehicleNumber));
        sb.append(String.format("Vehicle Type  : %s\n", vehicleType));
        sb.append(String.format("Floor         : %d\n", floorNumber));
        sb.append(String.format("Slot          : %s\n", slotId));
        sb.append(String.format("Entry Time    : %s\n", entryTime));
        if (exitTime != null) {
            sb.append(String.format("Exit Time     : %s\n", exitTime));
            sb.append(String.format("Duration      : %d hours\n", getParkingDurationHours()));
            sb.append(String.format("Fare          : Rs. %.2f\n", fare));
        }
        sb.append("========================================\n");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Ticket[%s | %s | %s | Status: %s]", 
            ticketId, vehicleNumber, slotId, status);
    }
}
