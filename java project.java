
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class VehicleRentalSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Vehicle> vehicles = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static int nextBookingId = 1001;

    // Vehicle class
    static class Vehicle {
        int id;
        String name;
        String type;
        double pricePerDay;
        boolean available;

        Vehicle(int id, String name, String type, double pricePerDay) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.pricePerDay = pricePerDay;
            this.available = true;
        }

        void display() {
            System.out.printf(
                "%-5d %-18s %-12s Rs. %-10.2f %-10s%n",
                id, name, type, pricePerDay,
                available ? "Available" : "Rented"
            );
        }
    }

    // Booking class
    static class Booking {
        int bookingId;
        String customerName;
        Vehicle vehicle;
        LocalDate bookingDate;
        int days;
        double totalAmount;
        String status;
        String paymentMethod;
        String paymentStatus;

        Booking(int bookingId, String customerName,
                Vehicle vehicle, int days) {
            this.bookingId = bookingId;
            this.customerName = customerName;
            this.vehicle = vehicle;
            this.bookingDate = LocalDate.now();
            this.days = days;
            this.totalAmount = vehicle.pricePerDay * days;
            this.status = "ACTIVE";
            this.paymentMethod = "Not selected";
            this.paymentStatus = "PENDING";
        }

        void display() {
            System.out.println("----------------------------------------");
            System.out.println("Booking ID     : " + bookingId);
            System.out.println("Customer       : " + customerName);
            System.out.println("Vehicle        : " + vehicle.name);
            System.out.println("Booking Date   : " + bookingDate);
            System.out.println("Rental Days    : " + days);
            System.out.printf("Price Per Day  : Rs. %.2f%n",
                    vehicle.pricePerDay);
            System.out.printf("Total Amount   : Rs. %.2f%n",
                    totalAmount);
            System.out.println("Booking Status : " + status);
            System.out.println("Payment Method : " + paymentMethod);
            System.out.println("Payment Status : " + paymentStatus);
            System.out.println("----------------------------------------");
        }
    }

    // Initialize sample vehicles
    static void initializeVehicles() {
        vehicles.add(new Vehicle(1, "Maruti Swift", "Car", 1800));
        vehicles.add(new Vehicle(2, "Hyundai i20", "Car", 2200));
        vehicles.add(new Vehicle(3, "Toyota Innova", "SUV", 3500));
        vehicles.add(new Vehicle(4, "Honda Activa", "Bike", 500));
        vehicles.add(new Vehicle(5, "Royal Enfield", "Bike", 1000));
    }

    // Display all vehicles
    static void displayVehicles(boolean availableOnly) {
        System.out.println("\n========== VEHICLE LIST ==========");
        System.out.printf("%-5s %-18s %-12s %-14s %-10s%n",
                "ID", "Vehicle", "Type", "Price/Day", "Status");

        for (Vehicle v : vehicles) {
            if (!availableOnly || v.available) {
                v.display();
            }
        }
    }

    // Find vehicle by ID
    static Vehicle findVehicle(int id) {
        for (Vehicle v : vehicles) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }

    // Find booking by ID
    static Booking findBooking(int id) {
        for (Booking b : bookings) {
            if (b.bookingId == id) {
                return b;
            }
        }
        return null;
    }

    // Create a booking
    static void bookVehicle() {
        displayVehicles(true);

        System.out.print("Enter vehicle ID: ");
        int vehicleId = readInt();

        Vehicle vehicle = findVehicle(vehicleId);

        if (vehicle == null || !vehicle.available) {
            System.out.println("Invalid vehicle or vehicle unavailable.");
            return;
        }

        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        System.out.print("Enter rental days: ");
        int days = readInt();

        if (days <= 0) {
            System.out.println("Rental days must be greater than zero.");
            return;
        }

        Booking booking = new Booking(
                nextBookingId++, customerName, vehicle, days);

        bookings.add(booking);
        vehicle.available = false;

        System.out.println("\nBooking created successfully!");
        booking.display();
        System.out.println("Please complete payment using option 3.");
    }

    // Process simulated payment
    static void makePayment() {
        System.out.print("Enter booking ID: ");
        int id = readInt();

        Booking booking = findBooking(id);

        if (booking == null || !booking.status.equals("ACTIVE")) {
            System.out.println("Active booking not found.");
            return;
        }

        if (booking.paymentStatus.equals("PAID")) {
            System.out.println("Payment has already been completed.");
            return;
        }

        if (booking.paymentStatus.equals("REFUNDED")) {
            System.out.println("This booking has been refunded.");
            return;
        }

        System.out.println("\nPayment Amount: Rs. "
                + String.format("%.2f", booking.totalAmount));
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");
        System.out.print("Choose payment method: ");

        int choice = readInt();
        String method;

        switch (choice) {
            case 1:
                method = "Cash";
                break;
            case 2:
                method = "UPI";
                break;
            case 3:
                method = "Card";
                break;
            default:
                System.out.println("Invalid payment method.");
                return;
        }

        // Demonstration only: no real money is collected.
        System.out.print("Confirm simulated payment? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Payment cancelled.");
            return;
        }

        booking.paymentMethod = method;
        booking.paymentStatus = "PAID";

        System.out.println("\nPayment successful!");
        System.out.println("Transaction Reference: TXN-" + booking.bookingId);
        System.out.println("Method: " + method);
        System.out.printf("Amount Paid: Rs. %.2f%n",
                booking.totalAmount);
    }

    // View a specific booking
    static void viewBooking() {
        System.out.print("Enter booking ID: ");
        int id = readInt();

        Booking booking = findBooking(id);

        if (booking == null) {
            System.out.println("Booking not found.");
        } else {
            booking.display();
        }
    }

    // View all bookings
    static void viewAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("\n========== ALL BOOKINGS ==========");
        for (Booking booking : bookings) {
            booking.display();
        }
    }

    // Cancel a booking
    static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        int id = readInt();

        Booking booking = findBooking(id);

        if (booking == null || !booking.status.equals("ACTIVE")) {
            System.out.println("Active booking not found.");
            return;
        }

        booking.status = "CANCELLED";
        booking.vehicle.available = true;

        if (booking.paymentStatus.equals("PAID")) {
            // Simulated refund; no real financial transaction occurs.
            booking.paymentStatus = "REFUNDED";
            System.out.println(
                "Booking cancelled. Payment marked as refunded.");
        } else {
            System.out.println("Booking cancelled successfully.");
        }
    }

    // Display payment report
    static void paymentReport() {
        System.out.println("\n========== PAYMENT REPORT ==========");

        double paidAmount = 0;
        double refundedAmount = 0;

        if (bookings.isEmpty()) {
            System.out.println("No payment records available.");
            return;
        }

        for (Booking b : bookings) {
            System.out.println(
                "Booking ID: " + b.bookingId
                + " | Customer: " + b.customerName
                + " | Amount: Rs. "
                + String.format("%.2f", b.totalAmount)
                + " | Method: " + b.paymentMethod
                + " | Status: " + b.paymentStatus
            );

            if (b.paymentStatus.equals("PAID")) {
                paidAmount += b.totalAmount;
            } else if (b.paymentStatus.equals("REFUNDED")) {
                refundedAmount += b.totalAmount;
            }
        }

        System.out.printf("%nTotal successful payments: Rs. %.2f%n",
                paidAmount);
        System.out.printf("Total simulated refunds: Rs. %.2f%n",
                refundedAmount);
        System.out.printf("Net collected amount: Rs. %.2f%n",
                paidAmount);
    }

    // Read integer safely
    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.nextLine();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    // Main menu
    public static void main(String[] args) {
        initializeVehicles();

        int choice;

        do {
            System.out.println("\n========================================");
            System.out.println(" VEHICLE RENTAL & PAYMENT MANAGEMENT");
            System.out.println("========================================");
            System.out.println("1. View All Vehicles");
            System.out.println("2. Book a Vehicle");
            System.out.println("3. Make Payment");
            System.out.println("4. View Booking Details");
            System.out.println("5. View All Bookings");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Payment Report");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    displayVehicles(false);
                    break;
                case 2:
                    bookVehicle();
                    break;
                case 3:
                    makePayment();
                    break;
                case 4:
                    viewBooking();
                    break;
                case 5:
                    viewAllBookings();
                    break;
                case 6:
                    cancelBooking();
                    break;
                case 7:
                    paymentReport();
                    break;
                case 8:
                    System.out.println(
                        "Thank you for using Vehicle Rental System!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        sc.close();
    }
}
