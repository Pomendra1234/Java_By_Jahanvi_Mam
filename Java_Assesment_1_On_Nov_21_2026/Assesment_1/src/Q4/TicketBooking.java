package Q4;


/*
Q4. Multithreading – Ticket Booking Problem (15 Marks)
A ticket-booking system has 100 available tickets. Five threads simultaneously attempt to book 30 tickets each. Develop a Java program that:
6.	Creates five booking threads.
7.	Prevents overbooking and ensures thread-safe access to the shared ticket count.
8.	Allows a booking only when enough tickets are available.
9.	Displays successful and failed booking attempts.
10.	Displays the final number of available tickets.
Interview challenge: Explain why synchronization is required and identify the race condition that could occur without it.

*/


public class TicketBooking {

    static int availableTickets = 100;   // shared ticket count
    static int successCount = 0;
    static int failCount = 0;

    public static void main(String[] args) throws InterruptedException {

        // ================= Round 1 ka kaam: BINA synchronization =================
        Runnable unsafeTask = () -> {
            int request = 30;
            String name = Thread.currentThread().getName();

            if (availableTickets >= request) {             // check
                try {
                    Thread.sleep(100);                     // check aur update ke beech ka gap
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                availableTickets = availableTickets - request;   // update
                successCount++;
                System.out.println(name + " -> SUCCESS: " + request + " tickets book hue. Bache: " + availableTickets);
            } else {
                failCount++;
                System.out.println(name + " -> FAILED : " + request + " tickets chahiye the, sirf " + availableTickets + " bache hain.");
            }
        };

        // ================= Round 2 ka kaam: synchronized ke SAATH =================
        Runnable safeTask = () -> {
            int request = 30;
            String name = Thread.currentThread().getName();

            synchronized (TicketBooking.class) {           // ek time par ek hi thread andar
                if (availableTickets >= request) {         // check
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    availableTickets = availableTickets - request;   // update
                    successCount++;
                    System.out.println(name + " -> SUCCESS: " + request + " tickets book hue. Bache: " + availableTickets);
                } else {
                    failCount++;
                    System.out.println(name + " -> FAILED : " + request + " tickets chahiye the, sirf " + availableTickets + " bache hain.");
                }
            }
        };

        // ==================================================
        // ROUND 1: WITHOUT synchronization (race condition)
        // ==================================================
        System.out.println("===== ROUND 1: WITHOUT synchronization =====");

        Thread u1 = new Thread(unsafeTask, "Customer-1");
        Thread u2 = new Thread(unsafeTask, "Customer-2");
        Thread u3 = new Thread(unsafeTask, "Customer-3");
        Thread u4 = new Thread(unsafeTask, "Customer-4");
        Thread u5 = new Thread(unsafeTask, "Customer-5");

        u1.start();
        u2.start();
        u3.start();
        u4.start();
        u5.start();

        u1.join();
        u2.join();
        u3.join();
        u4.join();
        u5.join();

        System.out.println("\n----- Round 1 Result -----");
        System.out.println("Successful bookings     : " + successCount);
        System.out.println("Failed bookings         : " + failCount);
        System.out.println("Final available tickets : " + availableTickets);
        if (availableTickets < 0) {
            System.out.println(">>> OVERBOOKING HO GAYI! Tickets negative ho gaye (race condition).");
        }

        // Round 2 ke liye reset
        availableTickets = 100;
        successCount = 0;
        failCount = 0;

        // ==================================================
        // ROUND 2: WITH synchronization (thread-safe)
        // ==================================================
        System.out.println("\n===== ROUND 2: WITH synchronization =====");

        Thread s1 = new Thread(safeTask, "Customer-1");
        Thread s2 = new Thread(safeTask, "Customer-2");
        Thread s3 = new Thread(safeTask, "Customer-3");
        Thread s4 = new Thread(safeTask, "Customer-4");
        Thread s5 = new Thread(safeTask, "Customer-5");

        s1.start();
        s2.start();
        s3.start();
        s4.start();
        s5.start();

        s1.join();
        s2.join();
        s3.join();
        s4.join();
        s5.join();

        System.out.println("\n----- Round 2 Result -----");
        System.out.println("Successful bookings     : " + successCount);
        System.out.println("Failed bookings         : " + failCount);
        System.out.println("Final available tickets : " + availableTickets);
    }
}
