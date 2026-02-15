package com.parking.service.interfaces;

import com.parking.model.Ticket;

/**
 * Interface for fee calculation strategies.
 * Dependency Inversion: Services depend on this abstraction, not concrete implementations.
 */
public interface FeeCalculator {
    
    /**
     * Calculate parking fee for a ticket.
     * @param ticket The parking ticket
     * @return The calculated fee amount
     */
    double calculateFee(Ticket ticket);
}
