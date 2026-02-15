package com.parking.service.strategy;

import com.parking.model.ParkingLot;
import com.parking.model.Ticket;
import com.parking.model.VehicleType;
import com.parking.service.interfaces.FeeCalculator;

/**
 * Standard hourly fee calculator.
 * Charges based on hours parked * hourly rate for vehicle type.
 */
public class HourlyFeeCalculator implements FeeCalculator {
    
    private ParkingLot parkingLot;
    
    public HourlyFeeCalculator() {
        this.parkingLot = ParkingLot.getInstance();
    }
    
    @Override
    public double calculateFee(Ticket ticket) {
        if (ticket == null) {
            return 0;
        }
        
        long hours = ticket.getParkingDurationHours();
        VehicleType type = ticket.getVehicleType();
        double hourlyRate = parkingLot.getHourlyRate(type);
        
        // If vehicle type not set in ticket, use default
        if (type == null) {
            hourlyRate = 20.0; // default rate
        }
        
        return hours * hourlyRate;
    }
}
