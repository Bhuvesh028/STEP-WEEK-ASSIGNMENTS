 import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        occupied = false;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity = 500;
    private int size = 0;
    private int totalProbes = 0;

    public ParkingLot() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }


    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }


    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        size++;
        totalProbes += probes;

        System.out.println("Vehicle " + licensePlate +
                " → Assigned spot #" + index +
                " (" + probes + " probes)");
    }


    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long duration = (exitTime - table[index].entryTime) / 1000;

                double fee = duration * 0.05;

                table[index].occupied = false;
                size--;

                System.out.println("Vehicle " + licensePlate +
                        " exited. Spot #" + index +
                        " freed. Fee: $" + String.format("%.2f", fee));
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }


    public void getStatistics() {

        double occupancy = (size * 100.0) / capacity;
        double avgProbes = size == 0 ? 0 : (totalProbes * 1.0) / size;

        System.out.println("Occupancy: " +
                String.format("%.2f", occupancy) + "%");

        System.out.println("Average Probes: " +
                String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}

