
package com.parking;

import com.parking.exception.*;
import com.parking.model.*;
import com.parking.service.core.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class ParkingApplication {

    private static Scanner sc;
    private static AdminService adminService;
    private static ParkingService parkingService;
    private static DisplayBoardService displayBoard;
    private static EntryGateHandler entryGate;
    private static ExitGateHandler exitGate;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        
        // Initialize services with dependency injection
        adminService = new AdminService();
        parkingService = new ParkingService();
        displayBoard = new DisplayBoardService();
        entryGate = new EntryGateHandler("ENTRY-1", parkingService);
        exitGate = new ExitGateHandler("EXIT-1", parkingService);
        
        // Configure parking lot
        ParkingLot.getInstance().setName("Smart Parking System");
        
        printWelcome();
        
        while (true) {
            printMainMenu();
            int mode = readInt();

            switch (mode) {
                case 1:
                    adminMode();
                    break;
                case 2:
                    securityMode();
                    break;
                case 3:
                    displayMode();
                    break;
                case 0:
                    System.out.println("\n╔═══════════════════════════════════════╗");
                    System.out.println("║     Thank you for using our system!   ║");
                    System.out.println("╚═══════════════════════════════════════╝");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printWelcome() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║           ★★★  SMART PARKING MANAGEMENT SYSTEM  ★★★.         ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static void printMainMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           MAIN MENU                 │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Admin Mode                      │");
        System.out.println("│  2. Security Mode (Park/Unpark)     │");
        System.out.println("│  3. Display Mode (View Status)      │");
        System.out.println("│  0. Exit                            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }

    private static void adminMode() {
        while (true) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│          ADMIN MENU                 │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│  1. Add Floors                      │");
            System.out.println("│  2. Delete Floor                    │");
            System.out.println("│  3. Slot Management                 │");
            System.out.println("│  4. View All Floors & Slots         │");
            System.out.println("│  5. View Single Floor Slots         │");
            System.out.println("│  6. Set Hourly Rates                │");
            System.out.println("│  7. View Current Rates              │");
            System.out.println("│  8. Configure Parking Lot           │");
            System.out.println("│  9. View Statistics                 │");
            System.out.println("│  0. Back to Main Menu               │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    addFloors();
                    break;
                case 2:
                    try {
                        deleteFloor();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 3:
                    slotManagement();
                    break;
                case 4:
                    try {
                        adminService.displayAllFloorsWithSlots();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        viewSingleFloorSlots();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 6:
                    setHourlyRates();
                    break;
                case 7:
                    adminService.displayRates();
                    break;
                case 8:
                    configureParkingLot();
                    break;
                case 9:
                    try {
                        adminService.displayStatus();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private static void slotManagement() {
        while (true) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│        SLOT MANAGEMENT              │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│  1. Add Slots to Floor              │");
            System.out.println("│  2. Delete Slots from Floor         │");
            System.out.println("│  3. Convert Slots (Change Type)     │");
            System.out.println("│  4. Edit Slot Properties            │");
            System.out.println("│  5. View Slot Details               │");
            System.out.println("│  0. Back to Admin Menu              │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("Enter your choice: ");
            
            int choice = readInt();
            
            switch (choice) {
                case 1:
                    try {
                        addSlotsToFloor();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        deleteSlotsFromFloor();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        convertSlots();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        editSlotProperties();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        viewSlotDetails();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private static void addSlotsToFloor() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        // First display available floors
        adminService.displayAllFloorsWithSlots();
        
        System.out.print("Floor number to add slots: ");
        int floorNum = readInt();
        
        System.out.println("Vehicle type:");
        System.out.println("  2 - Two Wheeler (Bike/Scooter)");
        System.out.println("  4 - Four Wheeler (Car/SUV)");
        System.out.println("  6 - Six Wheeler (Bus/Truck)");
        System.out.print("Enter type: ");
        int typeCode = readInt();
        
        VehicleType type = VehicleType.fromCode(typeCode);
        if (type == null) {
            System.out.println("Error: Invalid vehicle type.");
            return;
        }
        
        System.out.print("Number of slots to add: ");
        int count = readInt();
        
        adminService.addSlotsToFloor(floorNum, count, type);
    }
    
    private static void deleteSlotsFromFloor() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        // First display available floors
        adminService.displayAllFloorsWithSlots();
        
        System.out.print("Floor number to delete slots from: ");
        int floorNum = readInt();
        
        System.out.println("Vehicle type:");
        System.out.println("  2 - Two Wheeler");
        System.out.println("  4 - Four Wheeler");
        System.out.println("  6 - Six Wheeler");
        System.out.print("Enter type: ");
        int typeCode = readInt();
        
        VehicleType type = VehicleType.fromCode(typeCode);
        if (type == null) {
            System.out.println("Error: Invalid vehicle type.");
            return;
        }
        
        System.out.print("Number of slots to delete: ");
        int count = readInt();
        
        adminService.deleteSlotsFromFloor(floorNum, count, type);
    }
    
    private static void editSlotProperties() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        System.out.print("Enter Slot ID (e.g., F1-4W-S1): ");
        String slotId = sc.next();
        
        // Show current slot details
        adminService.displaySlotDetails(slotId);
        
        System.out.println("\nSet slot properties:");
        System.out.print("Is reserved slot? (y/n): ");
        boolean reserved = sc.next().toLowerCase().startsWith("y");
        
        adminService.editSlot(slotId, reserved);
    }
    
    private static void viewSlotDetails() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        System.out.print("Enter Slot ID (e.g., F1-4W-S1): ");
        String slotId = sc.next();
        adminService.displaySlotDetails(slotId);
    }
    
    private static void viewSingleFloorSlots() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        System.out.print("Enter floor number: ");
        int floorNum = readInt();
        adminService.displayFloorSlots(floorNum);
    }

    private static void addFloors() {
        System.out.print("Number of floors to add: ");
        int n = readInt();

        System.out.print("2W slots per floor: ");
        int two = readInt();

        System.out.print("4W slots per floor: ");
        int four = readInt();

        System.out.print("6W slots per floor: ");
        int six = readInt();

        adminService.addFloors(n, two, four, six);
    }

    private static void deleteFloor() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        System.out.print("Floor number to delete: ");
        int f = readInt();
        adminService.deleteFloor(f);
    }

    private static void convertSlots() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException("No floors exist. Please add floors first.");
        }
        
        System.out.print("Floor number: ");
        int f = readInt();

        System.out.print("Convert FROM type (2/4/6): ");
        int from = readInt();

        System.out.print("Convert TO type (2/4/6): ");
        int to = readInt();

        System.out.print("Number of slots to convert: ");
        int count = readInt();

        adminService.convertSlots(f, from, to, count);
    }

    private static void setHourlyRates() {
        System.out.println("\nSet hourly rate for which vehicle type?");
        System.out.println("  1. Two Wheeler (2W)");
        System.out.println("  2. Four Wheeler (4W)");
        System.out.println("  3. Six Wheeler (6W)");
        System.out.print("Choice: ");
        int typeChoice = readInt();
        
        VehicleType type = null;
        switch (typeChoice) {
            case 1: type = VehicleType.TWO_WHEELER; break;
            case 2: type = VehicleType.FOUR_WHEELER; break;
            case 3: type = VehicleType.SIX_WHEELER; break;
            default: 
                System.out.println("Invalid vehicle type.");
                return;
        }
        
        System.out.print("Enter new hourly rate (Rs.): ");
        double rate = readDouble();
        
        adminService.setHourlyRate(type, rate);
    }

    private static void configureParkingLot() {
        System.out.print("Parking Lot Name: ");
        sc.nextLine(); // consume newline
        String name = sc.nextLine();
        
        System.out.print("Address: ");
        String address = sc.nextLine();
        
        adminService.configureParkingLot(name, address);
    }

    private static void securityMode() {
        while (true) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│        SECURITY MODE                │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│  1. Park Vehicle (Entry Gate)       │");
            System.out.println("│  2. Unpark Vehicle (Exit Gate)      │");
            System.out.println("│  3. Lost Ticket                     │");
            System.out.println("│  4. Check Vehicle Status            │");
            System.out.println("│  5. Quick Availability Check        │");
            System.out.println("│  0. Back to Main Menu               │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("Enter your choice: ");

            int c = readInt();

            switch (c) {
                case 1:
                    try {
                        parkVehicle();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        unparkVehicle();
                    } catch (NoVehiclesParkedException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    } catch (InvalidTicketException e) {
                        System.out.println("\n⚠ " + e.getMessage() + ". Please contact staff.");
                    }
                    break;
                case 3:
                    try {
                        handleLostTicket();
                    } catch (NoVehiclesParkedException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    } catch (VehicleNotFoundException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        checkVehicleStatus();
                    } catch (NoVehiclesParkedException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        entryGate.displayAvailability();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void parkVehicle() {
        if (ParkingLot.getInstance().getFloors().isEmpty()) {
            throw new NoFloorsConfiguredException();
        }
        
        sc.nextLine(); // clear buffer
        System.out.print("Vehicle number: ");
        String number = sc.nextLine().trim();

        System.out.print("Vehicle color: ");
        String color = sc.nextLine().trim();

        System.out.println("Vehicle type:");
        System.out.println("  2 - Two Wheeler (Bike/Scooter)");
        System.out.println("  4 - Four Wheeler (Car/SUV)");
        System.out.println("  6 - Six Wheeler (Bus/Truck)");
        System.out.print("Enter type: ");
        int type = readInt();

        try {
            Vehicle vehicle = VehicleFactory.createVehicle(number, color, type);
            entryGate.processEntry(vehicle);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid vehicle type.");
        }
    }

    private static void unparkVehicle() {
        if (ParkingLot.getInstance().getActiveTickets().isEmpty()) {
            throw new NoVehiclesParkedException();
        }
        
        System.out.print("Ticket ID: ");
        String id = sc.next();
        exitGate.processExit(id);
    }

    private static void handleLostTicket() {
        if (ParkingLot.getInstance().getActiveTickets().isEmpty()) {
            throw new NoVehiclesParkedException();
        }
        
        sc.nextLine(); // clear buffer
        System.out.print("Vehicle number: ");
        String vehicleNumber = sc.nextLine().trim();
        exitGate.processLostTicket(vehicleNumber);
    }

    private static void checkVehicleStatus() {
        if (ParkingLot.getInstance().getActiveTickets().isEmpty()) {
            throw new NoVehiclesParkedException();
        }
        
        sc.nextLine(); // clear buffer
        System.out.print("Vehicle number: ");
        String vehicleNumber = sc.nextLine().trim();
        
        Ticket ticket = parkingService.findTicketByVehicle(vehicleNumber);
        
        if (ticket == null) {
            System.out.println("\n⚠ Vehicle " + vehicleNumber + " is not currently parked.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String entryTimeStr = ticket.getEntryTime().format(formatter);
            String exitTimeStr = ticket.getExitTime() != null ? ticket.getExitTime().format(formatter) : "Still Parked";
            
            System.out.println("\n╔═════════════════════════════════════════════╗");
            System.out.println("║           VEHICLE STATUS                    ║");
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.printf("║  Vehicle   : %-30s║%n", ticket.getVehicleNumber());
            System.out.printf("║  Ticket    : %-30s║%n", ticket.getTicketId());
            System.out.printf("║  Slot      : %-30s║%n", ticket.getSlotId());
            System.out.printf("║  Entry Time: %-30s║%n", entryTimeStr);
            System.out.printf("║  Exit Time : %-30s║%n", exitTimeStr);
            System.out.printf("║  Duration  : %-30s║%n", ticket.getParkingDurationHours() + " hours");
            System.out.println("╚═════════════════════════════════════════════╝");
        }
    }

    private static void displayMode() {
        while (true) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│         DISPLAY MODE                │");
            System.out.println("├─────────────────────────────────────┤");
            System.out.println("│  1. Full Display Board              │");
            System.out.println("│  2. Floor-wise Availability         │");
            System.out.println("│  3. Compact View                    │");
            System.out.println("│  4. Floor Slot Map                  │");
            System.out.println("│  5. Parking Lot Status              │");
            System.out.println("│  0. Back to Main Menu               │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    try {
                        displayBoard.showAllFloorsAvailability();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        parkingService.displayFloorAvailability();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        displayBoard.showCompactView();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        if (ParkingLot.getInstance().getFloors().isEmpty()) {
                            throw new NoFloorsConfiguredException();
                        }
                        System.out.print("Enter floor number: ");
                        int floorNum = readInt();
                        displayBoard.showSlotMap(floorNum);
                    } catch (NoFloorsConfiguredException | FloorNotFoundException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        adminService.displayStatus();
                    } catch (NoFloorsConfiguredException e) {
                        System.out.println("\n⚠ " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Utility methods for safe input reading
    private static int readInt() {
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // clear buffer
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return sc.nextDouble();
        } catch (Exception e) {
            sc.nextLine(); // clear buffer
            return -1;
        }
    }
}
