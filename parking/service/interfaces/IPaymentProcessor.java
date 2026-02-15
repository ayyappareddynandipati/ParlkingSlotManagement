package com.parking.service.interfaces;

import com.parking.model.Ticket;

/**
 * Interface for Payment Processing operations.
 * Dependency Inversion: Exit handlers depend on this abstraction for payment.
 */
public interface IPaymentProcessor {
    
    /**
     * Process payment for a parking ticket.
     * @param ticket The parking ticket
     * @param amount The amount to pay
     * @return true if payment was successful
     */
    boolean processPayment(Ticket ticket, double amount);
    
    /**
     * Process refund.
     * @param ticketId The ticket ID
     * @param amount The refund amount
     * @return true if refund was successful
     */
    boolean processRefund(String ticketId, double amount);
    
    /**
     * Check payment status.
     * @param ticketId The ticket ID
     * @return Payment status description
     */
    String getPaymentStatus(String ticketId);
    
    /**
     * Get payment receipt.
     * @param ticketId The ticket ID
     * @return Payment receipt as string
     */
    String generateReceipt(String ticketId);
    
    /**
     * Get total revenue.
     * @return Total revenue collected
     */
    double getTotalRevenue();
    
    /**
     * Get revenue for a specific period.
     * @param days Number of days to look back
     * @return Revenue for the period
     */
    double getRevenueForPeriod(int days);
}
