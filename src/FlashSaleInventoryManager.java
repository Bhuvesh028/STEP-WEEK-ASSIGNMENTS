import java.util.*;

public class FlashSaleInventoryManager {


    private HashMap<String, Integer> inventory = new HashMap<>();


    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();


    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }


    public synchronized void checkStock(String productId) {
        int stock = inventory.getOrDefault(productId, 0);
        System.out.println(productId + " → " + stock + " units available");
    }


    public synchronized void purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            stock--;
            inventory.put(productId, stock);

            System.out.println("User " + userId + " → Purchase Success, "
                    + stock + " units remaining");
        }
        else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            System.out.println("User " + userId
                    + " → Added to waiting list, position #" + queue.size());
        }
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("IPHONE15_256GB", 3);

        manager.checkStock("IPHONE15_256GB");

        manager.purchaseItem("IPHONE15_256GB", 12345);
        manager.purchaseItem("IPHONE15_256GB", 67890);
        manager.purchaseItem("IPHONE15_256GB", 11111);
        manager.purchaseItem("IPHONE15_256GB", 99999);

        manager.checkStock("IPHONE15_256GB");
    }
}