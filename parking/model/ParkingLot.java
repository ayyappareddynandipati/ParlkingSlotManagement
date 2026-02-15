package com.parking.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    
    private static ParkingLot instance;
    
    private String name;
    private String address;
    private List<Floor> floors;
    private Map<String, Ticket> activeTickets;
    private Map<String, Ticket> archivedTickets;
    private Map<VehicleType, Double> hourlyRates;
    private int maxCapacity;
    
    private ParkingLot() {
        this.floors = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.archivedTickets = new HashMap<>();
        this.hourlyRates = new HashMap<>();
        
        // Initialize default hourly rates
        for (VehicleType type : VehicleType.values()) {
            hourlyRates.put(type, type.getDefaultHourlyRate());
        }
    }
    
    /**
     * Get the singleton instance of ParkingLot.
     */
    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }
    
    /**
     * Reset the singleton instance (for testing purposes).
     */
    public static synchronized void resetInstance() {
        instance = null;
    }
    
    // Configuration methods
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public List<Floor> getFloors() {
        return floors;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    // Floor management
    public void addFloor(Floor floor) {
        floors.add(floor);
    }
    
    public boolean removeFloor(int floorNumber) {
        return floors.removeIf(f -> f.getFloorNumber() == floorNumber && !f.hasOccupiedSlots());
    }
    
    public Floor getFloor(int floorNumber) {
        for (Floor f : floors) {
            if (f.getFloorNumber() == floorNumber) {
                return f;
            }
        }
        return null;
    }
    
    // Ticket management
    public void addActiveTicket(Ticket ticket) {
        activeTickets.put(ticket.getTicketId(), ticket);
    }
    
    public Ticket getActiveTicket(String ticketId) {
        return activeTickets.get(ticketId);
    }
    
    public void archiveTicket(String ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket != null) {
            archivedTickets.put(ticketId, ticket);
        }
    }
    
    public Map<String, Ticket> getActiveTickets() {
        return new HashMap<>(activeTickets);
    }
    
    public boolean isVehicleParked(String vehicleNumber) {
        for (Ticket ticket : activeTickets.values()) {
            if (ticket.getVehicleNumber().equals(vehicleNumber) && ticket.isActive()) {
                return true;
            }
        }
        return false;
    }
    
    public Ticket findTicketByVehicle(String vehicleNumber) {
        for (Ticket ticket : activeTickets.values()) {
            if (ticket.getVehicleNumber().equals(vehicleNumber) && ticket.isActive()) {
                return ticket;
            }
        }
        return null;
    }
    
    // Rate management
    public void setHourlyRate(VehicleType type, double rate) {
        hourlyRates.put(type, rate);
    }
    
    public double getHourlyRate(VehicleType type) {
        return hourlyRates.getOrDefault(type, type.getDefaultHourlyRate());
    }
    
    public Map<VehicleType, Double> getAllHourlyRates() {
        return new HashMap<>(hourlyRates);
    }
    
    // Capacity calculations
    public int getTotalCapacity() {
        int total = 0;
        for (Floor floor : floors) {
            total += floor.getTotalSlots();
        }
        return total;
    }
    
    public int getTotalAvailableSlots() {
        int available = 0;
        for (Floor floor : floors) {
            available += floor.getAvailableSlots();
        }
        return available;
    }
    
    public int getTotalOccupiedSlots() {
        int occupied = 0;
        for (Floor floor : floors) {
            occupied += floor.getOccupiedSlots();
        }
        return occupied;
    }
    
    public int getAvailableSlotsByType(VehicleType type) {
        int available = 0;
        for (Floor floor : floors) {
            available += floor.countAvailableSlotsByType(type);
        }
        return available;
    }
    
    // Find slot
    public Slot findSlotById(String slotId) {
        for (Floor floor : floors) {
            Slot slot = floor.findSlotById(slotId);
            if (slot != null) {
                return slot;
            }
        }
        return null;
    }
    
    public boolean isFull() {
        return getTotalAvailableSlots() == 0;
    }
    
    public boolean isEmpty() {
        return getTotalOccupiedSlots() == 0;
    }
    
    /**
     * Get parking lot status summary.
     */
    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════════════════════════╗\n");
        sb.append("║                    PARKING LOT STATUS                    ║\n");
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        if (name != null) {
            sb.append(String.format("║  Name: %-51s║\n", name));
        }
        sb.append(String.format("║  Total Floors: %-43d║\n", floors.size()));
        sb.append(String.format("║  Total Capacity: %-41d║\n", getTotalCapacity()));
        sb.append(String.format("║  Available Slots: %-40d║\n", getTotalAvailableSlots()));
        sb.append(String.format("║  Occupied Slots: %-41d║\n", getTotalOccupiedSlots()));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append("║  AVAILABILITY BY VEHICLE TYPE:                           ║\n");
        for (VehicleType type : VehicleType.values()) {
            int available = getAvailableSlotsByType(type);
            sb.append(String.format("║    %s: %d available                                    ║\n", 
                type.getCode() + "W", available));
        }
        sb.append("╚══════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("ParkingLot[%s | Floors: %d | Capacity: %d]", 
            name, floors.size(), getTotalCapacity());
    }
}
