package com.mastering.lambdas.misc;

import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Here's another real-world example for the usage of Semaphore in Java: <b>A Parking Lot Management System</b>. <br>
 *
 * This is a perfect analogy for {@code Semaphore} because a parking lot has a fixed number of spaces, and cars (threads) must wait if the lot is full.
 *
 * <h2>Problem Scenario</h2>
 * A shopping mall has a parking lot with <b>only 5 parking spots</b>. When the lot is full, incoming cars <br>
 * must wait at the entrance until a spot becomes free. The system needs to:
 * <ul>
 *     <li>Track available spots</li>
 *     <li>Let cars enter if a spot is free</li>
 *     <li>Make waiting cars queue up fairly</li>
 *     <li>Count how many cars are currently parked</li>
 * </ul>
 *
 * <h2>Solution: Parking Lot with Semaphore</h2>
 *
 * <h3>Example Output (simplified):</h3>
 * <pre>{@code
 *     🏢 MALL OPENING SIMULATION
 * ==========================
 * 📊 'Mall Central Parking' Status: 0 cars parked, 3 spots free (out of 3 total)
 *
 * [12:00:01] 🚗 Car CAR-1 arrived at 'Mall Central Parking'. Trying to park...
 * [12:00:01] ✅ Car CAR-1 PARKED. (Currently parked: 1, Spots left: 2)
 *
 * [12:00:02] 🚗 Car CAR-2 arrived at 'Mall Central Parking'. Trying to park...
 * [12:00:02] ✅ Car CAR-2 PARKED. (Currently parked: 2, Spots left: 1)
 *
 * [12:00:03] 🚗 Car CAR-3 arrived at 'Mall Central Parking'. Trying to park...
 * [12:00:03] ✅ Car CAR-3 PARKED. (Currently parked: 3, Spots left: 0)
 *
 * [12:00:04] 🚗 Car CAR-4 arrived at 'Mall Central Parking'. Trying to park...
 * [12:00:04] ⏰ Car CAR-4 is WAITING (lot is full)...
 *
 * [12:00:05] 🚗 Car CAR-5 arrived at 'Mall Central Parking'. Trying to park...
 * [12:00:05] ⏰ Car CAR-5 is WAITING (lot is full)...
 *
 * [12:00:06] 🚪 Car CAR-2 LEFT. (Still parked: 2, Spots now available: 1)
 * [12:00:06] ✅ Car CAR-4 PARKED. (Currently parked: 3, Spots left: 0)
 *
 * [12:00:07] 🚪 Car CAR-1 LEFT. (Still parked: 2, Spots now available: 1)
 * [12:00:07] ✅ Car CAR-5 PARKED. (Currently parked: 3, Spots left: 0)
 *
 * ...
 * }</pre>
 *
 *
 * <h2>Key Features Demonstrated</h2>
 *
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <caption><b>Concurrency Features Overview</b></caption>
 *   <tr>
 *     <th>Feature</th>
 *     <th>Code</th>
 *     <th>What it does</th>
 *   </tr>
 *   <tr>
 *     <td>Fair waiting</td>
 *     <td>{@code new Semaphore(3, true)}</td>
 *     <td>Cars wait in FIFO order — first come, first served</td>
 *   </tr>
 *   <tr>
 *     <td>Timeout</td>
 *     <td>{@code tryAcquire(maxWaitSeconds, TimeUnit.SECONDS)}</td>
 *     <td>Car leaves if no spot within 8 seconds</td>
 *   </tr>
 *   <tr>
 *     <td>Non-blocking status</td>
 *     <td>{@code availablePermits()}</td>
 *     <td>Check available spots without acquiring</td>
 *   </tr>
 *   <tr>
 *     <td>Thread-safe counting</td>
 *     <td>{@code AtomicInteger}</td>
 *     <td>Track parked cars without race conditions</td>
 *   </tr>
 *   <tr>
 *     <td>Realistic simulation</td>
 *     <td>{@code Random shopping times + staggered arrivals}</td>
 *     <td>Mimics real parking behavior</td>
 *   </tr>
 * </table>
 *
 * <h2>Additional Real-World Feature: VIP Parking (Different Permits)</h2>
 * <h3>Here's an extension showing how you could have <b>regular spots</b> and <b>VIP spots:</b></h3>
 * <pre>{@code
 * public class PremiumParkingLot {
 *     private final Semaphore regularSpots;
 *     private final Semaphore vipSpots;
 *
 *     public PremiumParkingLot(int regularCount, int vipCount) {
 *         this.regularSpots = new Semaphore(regularCount, true);
 *         this.vipSpots = new Semaphore(vipCount, true);
 *     }
 *
 *     public boolean parkCar(String carId, boolean isVip, int maxWaitSeconds) {
 *         try {
 *             if (isVip) {
 *                 // VIP cars try VIP spots first
 *                 if (vipSpots.tryAcquire(maxWaitSeconds / 2, TimeUnit.SECONDS)) {
 *                     System.out.printf("👑 VIP Car %s parked in VIP section%n", carId);
 *                     // ... parking logic
 *                     return true;
 *                 }
 *                 // If no VIP spots, fall back to regular
 *                 System.out.printf("👑 VIP Car %s using regular spot%n", carId);
 *             }
 *
 *             // Regular parking logic
 *             regularSpots.tryAcquire(maxWaitSeconds, TimeUnit.SECONDS);
 *             // ...
 *         } catch (InterruptedException e) {
 *             return false;
 *         }
 *         return true;
 *     }
 * }
 * }</pre>
 *
 * <h2>Real Systems That Use This Pattern</h2>
 *
 * <ul>
 *   <li>
 *     <b>Airport parking counters</b> –
 *     Display {@code "FULL"} and open gates when spots become available
 *   </li>
 *   <li>
 *     <b>Cloud resource pools</b> –
 *     Limit EC2 instances or database connections
 *   </li>
 *   <li>
 *     <b>Ticket selling systems</b> –
 *     Restrict concurrent ticket purchases to available seats
 *   </li>
 *   <li>
 *     <b>Elevator systems</b> –
 *     Limit the number of people in an elevator (weight-based permits)
 *   </li>
 * </ul>
 *
 * <p>
 * The parking lot example is particularly effective because it is intuitive:
 * <ul>
 *   <li>{@code permits} = parking spots</li>
 *   <li>{@code acquire()} = park</li>
 *   <li>{@code release()} = leave</li>
 * </ul>
 * </p>
 *
 * <p>
 * This analogy is easy to understand, even for non-technical stakeholders.
 * </p>
 */
public class ParkingLot {

    // Semaphore represents the number of available parking spots
    private final Semaphore parkingSpots;

    // Atomic counter to track how many cars are actually parked (for reporting)
    private final AtomicInteger carsParked = new AtomicInteger(0);

    // Name of the parking lot
    private final String lotName;

    public ParkingLot(int totalSpots, String lotName) {
        this.parkingSpots = new Semaphore(totalSpots, true); // Fair mode = FIFO
        this.lotName = lotName;
    }

    /**
     * A car tries to enter the parking lot.
     *
     * @param carId Unique identifier for the car
     * @return true if the car parked successfully, false if it gave up waiting
     */
    public boolean parkCar(String carId, int maxWaitSeconds) {
        try {
            System.out.printf("[%s] 🚗 Car %s arrived at '%s'. Trying to park...%n",
                    LocalTime.now(), carId, lotName);

            // Try to acquire a parking spot with a timeout
            boolean acquired = parkingSpots.tryAcquire(maxWaitSeconds, TimeUnit.SECONDS);

            if (!acquired) {
                System.out.printf("[%s] ⏰ Car %s gave up waiting and left.%n",
                        LocalTime.now(), carId);
                return false;
            }

            // Successfully parked!
            int currentlyParked = carsParked.incrementAndGet();
            int spotsLeft = parkingSpots.availablePermits();

            System.out.printf("[%s] ✅ Car %s PARKED. (Currently parked: %d, Spots left: %d)%n",
                    LocalTime.now(), carId, currentlyParked, spotsLeft);

            // Simulate shopping time (1-5 seconds)
            int shoppingTime = (int) (Math.random() * 4000) + 1000;
            Thread.sleep(shoppingTime);

            // Leave the parking lot
            leaveParking(carId);
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] ❌ Car %s was interrupted.%n", LocalTime.now(), carId);
            return false;
        }
    }

    private void leaveParking(String carId) {
        // Release the parking spot back to the pool
        parkingSpots.release();
        int stillParked = carsParked.decrementAndGet();
        int spotsAvailable = parkingSpots.availablePermits();

        System.out.printf("[%s] 🚪 Car %s LEFT. (Still parked: %d, Spots now available: %d)%n",
                LocalTime.now(), carId, stillParked, spotsAvailable);
    }

    /**
     * Get current status without modifying the semaphore
     */
    public void printStatus() {
        System.out.printf("📊 '%s' Status: %d cars parked, %d spots free (out of %d total)%n",
                lotName,
                carsParked.get(),
                parkingSpots.availablePermits(),
                carsParked.get() + parkingSpots.availablePermits());
    }

    // ============ DEMO ============
    public static void main(String[] args) {
        // Create a parking lot with only 3 spots (easier to see the effect)
        ParkingLot mallParking = new ParkingLot(3, "Mall Central Parking");

        System.out.println("🏢 MALL OPENING SIMULATION");
        System.out.println("==========================");
        mallParking.printStatus();
        System.out.println();

        // Simulate 10 cars arriving at different times
        for (int i = 1; i <= 10; i++) {
            final String carId = "CAR-" + i;

            new Thread(() -> {
                // Cars are willing to wait up to 8 seconds for a spot
                mallParking.parkCar(carId, 8);
            }).start();

            // Stagger arrivals (0.5 to 2 seconds between cars)
            try {
                Thread.sleep((long) (Math.random() * 1500) + 500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Let the simulation run for a while
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        System.out.println("\n🏢 MALL CLOSING SUMMARY");
        System.out.println("=====================");
        mallParking.printStatus();
    }
}

/**
 * <h2>Practical Example: Using Semaphore in Java</h2>
 *
 * <p>
 * This example demonstrates a practical use of {@code Semaphore} in Java
 * to solve a real-world concurrency problem: <b>limiting the number of concurrent connections to a shared resource</b>.
 * </p>
 *
 * <p>
 * In this scenario, we simulate a printer server that can process only
 * <b>3 print jobs at the same time</b>. Any additional print requests
 * must wait until a printer becomes available.
 * </p>
 *
 * <h3>The Problem</h3>
 *
 * <p>
 * Without a semaphore, a system could receive 100 print jobs while having
 * only 3 physical printers available. This would overload the resource pool
 * and could lead to crashes, race conditions, or unpredictable behavior.
 * </p>
 *
 * <p>
 * A {@code Semaphore} acts as a <strong style="color:#ADFF2F">gatekeeper</strong> by allowing only a fixed number
 * of threads {@code N} to access the resource concurrently/simultaneously, ensuring controlled and
 * predictable execution.
 * </p>
 *
 * <h3>The Solution: Printer Server</h3>
 *
 * <p>
 * The following implementation uses a semaphore to limit concurrent print
 * operations and queue incoming print jobs safely.
 * </p>
 * <h3>Example Output</h3>
 * <pre>{@code
 * User 1 is waiting to print: Report_1.pdf
 * User 1 STARTED printing: Report_1.pdf (Active permits left: 2)
 * User 2 is waiting to print: Report_2.pdf
 * User 2 STARTED printing: Report_2.pdf (Active permits left: 1)
 * User 3 is waiting to print: Report_3.pdf
 * User 3 STARTED printing: Report_3.pdf (Active permits left: 0)
 * User 4 is waiting to print: Report_4.pdf
 * User 5 is waiting to print: Report_5.pdf
 * User 6 is waiting to print: Report_6.pdf
 * User 1 FINISHED printing: Report_1.pdf (Active permits left: 0)
 * User 4 STARTED printing: Report_4.pdf (Active permits left: 0)
 * User 2 FINISHED printing: Report_2.pdf (Active permits left: 0)
 * User 5 STARTED printing: Report_5.pdf (Active permits left: 0)
 * User 3 FINISHED printing: Report_3.pdf (Active permits left: 0)
 * User 6 STARTED printing: Report_6.pdf (Active permits left: 0)
 * ...
 * }
 *
 * </pre>
 *
 *
 * <h2>Explanation of key lines - Semaphore Operations Overview</h2>
 *
 * <table style="border-collapse: collapse; width: 100%;">
 *   <caption><b>Core Semaphore Methods</b></caption>
 *
 *   <tr style="border-bottom: 2px solid gray;">
 *     <th style="white-space: nowrap;" align="left">Code</th>
 *     <th align="left">What it does</th>
 *   </tr>
 *
 *   <tr style="border-bottom: 1px solid gray;">
 *     <td style="white-space: nowrap;">{@code new Semaphore(3, true)}</td>
 *     <td>
 *       Creates a semaphore with <b>3 permits</b>.
 *       {@code true} enables fairness, meaning threads receive permits
 *       in the order they requested them (FIFO).
 *     </td>
 *   </tr>
 *
 *   <tr style="border-bottom: 1px solid gray;">
 *     <td style="white-space: nowrap;">{@code semaphore.acquire()}</td>
 *     <td>
 *       Decreases the permit count by <b>1</b>.
 *       If no permits are available, the thread blocks and waits
 *       until one becomes free.
 *     </td>
 *   </tr>
 *
 *   <tr style="border-bottom: 1px solid gray;">
 *     <td style="white-space: nowrap;">{@code semaphore.release()}</td>
 *     <td>
 *       Increases the permit count by <b>1</b>.
 *       This wakes up waiting threads and allows them to continue.
 *     </td>
 *   </tr>
 *
 *   <tr style="border-bottom: 1px solid gray;">
 *     <td style="white-space: nowrap;">
 *       <span style="white-space: nowrap;">
 *         <code>semaphore&#8288;.&#8288;availablePermits()</code>
 *       </span>
 *     </td>
 *     <td>
 *       Returns the number of currently available permits.
 *       Mostly useful for debugging or demonstrations rather than production use.
 *     </td>
 *   </tr>
 * </table>
 *
 */
class PrinterServer {

    // Semaphore with 3 permits (3 printers available)
    private final Semaphore semaphore = new Semaphore(3, true); // true = fairness (FIFO)

    public void printDocument(String documentName, int userId) {
        try {
            // 1. Try to acquire a permit (wait if none available)
            System.out.printf("User %d is waiting to print: %s%n", userId, documentName);
            semaphore.acquire(); // Blocks until a permit is available

            // 2. Critical Section: Actually print the document
            System.out.printf("User %d STARTED printing: %s (Active permits left: %d)%n",
                    userId, documentName, semaphore.availablePermits());

            // Simulate printing time (1-3 seconds)
            TimeUnit.SECONDS.sleep((long) (Math.random() * 3) + 1);

            // 3. Release the permit back to the pool
            semaphore.release();
            System.out.printf("User %d FINISHED printing: %s (Active permits left: %d)%n",
                    userId, documentName, semaphore.availablePermits());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Printing was interrupted for user: " + userId);
        }
    }

    // Simulate multiple users trying to print
    public static void main(String[] args) {
        PrinterServer server = new PrinterServer();

        // Submit 10 print jobs from different users
        for (int userId = 1; userId <= 10; userId++) {
            final int id = userId;
            new Thread(() -> {
                server.printDocument("Report_" + id + ".pdf", id);
            }).start();

            // Small delay to make output readable
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
}

/**
 * <h1>Another Example: Rate Limiting API Calls</h1>
 * Here is a more modern example – limiting API calls to <b>5 per second</b>:
 * <pre>
 * <h2>Important Nuances</h2>
 * <ol>
 *     <li><strong>Fairness matters:</strong> Without fairness ({@code new Semaphore(3, false)}), a thread could theoretically wait <br>
 *         forever if other threads keep acquiring/releasing quickly. Use true for critical systems.</li>
 *     <li><strong>Always release in {@code finally} block:</strong> Just like with locks, always call {@code release()} in a {@code finally} block <br>
 *         to avoid semaphore leaks:
 * <pre>{@code
 *
 * semaphore.acquire();
 * try {
 *     // critical section
 * } finally {
 *     semaphore.release();
 * }
 * }</pre>
 * </li>
 * <li>{@code tryAcquire()} for <strong>non-blocking checks:</strong>
 * <pre>{@code
 *
 *     if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
 *         try {
 *             // got the permit within 1 second
 *         } finally {
 *             semaphore.release();
 *         }
 *     } else {
 *         System.out.println("Could not acquire permit, skipping task");
 *     }
 * }</li>
 * </ol>
 *  <h2>Real-world use cases for Semaphore</h2>
 *
 *  <table style="border-collapse: collapse; width: 100%;">
 *    <caption><b>Real-world use cases for Semaphore</b></caption>
 *
 *    <tr style="border-bottom: 2px solid gray;">
 *      <th style="white-space: nowrap;" align="left">Use Case</th>
 *      <th align="left" "white-space: nowrap;">Example</th>
 *    </tr>
 *
 *    <tr style="border-bottom: 1px solid gray;">
 *      <td style="white-space: nowrap;">Database connection pool</td>
 *      <td "white-space: nowrap;">
 *        Limit to 10 concurrent DB connections
 *      </td>
 *    </tr>
 *
 *    <tr style="border-bottom: 1px solid gray;">
 *      <td style="white-space: nowrap;">Throttling API calls</td>
 *      <td "white-space: nowrap;">
 *        Max 100 requests/minute to an external service
 *      </td>
 *    </tr>
 *
 *    <tr style="border-bottom: 1px solid gray;">
 *      <td style="white-space: nowrap;">Bounded buffer (Producer-Consumer)</td>
 *      <td "white-space: nowrap;">
 *        Control how many items can be in a queue
 *      </td>
 *    </tr>
 *
 *    <tr style="border-bottom: 1px solid gray;">
 *      <td style="white-space: nowrap;">
 *        Limiting file handles
 *      </td>
 *      <td "white-space: nowrap;">
 *        Don't open more than 50 files simultaneously. Sem. is gatekeeper.
 *      </td>
 *    </tr>
 *  </table>
 *  <br><br>
 *     <h2>Semaphore vs. Other Concurrency Tools</h2>
 *    <table style="border-collapse: collapse; width: 100%;">
 *     <caption><b>Semaphore vs. Other Concurrency Tools</b></caption>
 *
 *     <tr style="border-bottom: 2px solid gray;">
 *       <th style="white-space: nowrap;" align="left">Tool</th>
 *       <th align="left" "white-space: nowrap;">When to use</th>
 *     </tr>
 *
 *     <tr style="border-bottom: 1px solid gray;">
 *       <td style="white-space: nowrap;">{@code synchronized} / {@code Lock}</td>
 *       <td "white-space: nowrap;">
 *         Only <b>one</b> thread at a time (mutual exclusion)
 *       </td>
 *     </tr>
 *
 *     <tr style="border-bottom: 1px solid gray;">
 *       <td style="white-space: nowrap;">{@code Semaphore }</td>
 *       <td "white-space: nowrap;">
 *         <b>N</b> threads at a time (pooling, throttling)
 *       </td>
 *     </tr>
 *
 *     <tr style="border-bottom: 1px solid gray;">
 *       <td style="white-space: nowrap;">{@code CountDownLatch}</td>
 *       <td "white-space: nowrap;">
 *         Wait for N events to occur (one-time use)
 *       </td>
 *     </tr>
 *
 *     <tr style="border-bottom: 1px solid gray;">
 *      <td style="white-space: nowrap;">
 *         {@code CyclicBarrier}
 *       </td>
 *       <td "white-space: nowrap;">
 *         Wait until N threads reach a barrier (reusable)
 *       </td>
 *     </tr>
 *   </table>
 *   <p>In short: Use a {@code Semaphore} whenever you need to limit the <strong>number of concurrent users</strong> of a <br>
 *   resource, not just lock it completely.</p>
 */
class RateLimiter {
    private final Semaphore semaphore;

    public RateLimiter(int maxRequests) {
        this.semaphore = new Semaphore(maxRequests);
    }

    public void callApi(String endpoint) {
        try {
            semaphore.acquire();
            System.out.printf("[%s] Calling: %s (Remaining permits: %d)%n",
                    Instant.now(), endpoint, semaphore.availablePermits());
            // Simulate API call
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = new RateLimiter(5); // Max 5 concurrent calls
        for (int i = 1; i <= 20; i++) {
            final int id = i;
            new Thread(() -> limiter.callApi("/data/" + id)).start();
        }
    }
}