import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}class DNSCache {

    private HashMap<String, DNSEntry> cache = new HashMap<>();
    private int maxSize = 5;

    private int hits = 0;
    private int misses = 0;


    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println(domain + " → Cache HIT → " + entry.ipAddress);
            return entry.ipAddress;
        }


        misses++;
        String ip = queryUpstreamDNS(domain);


        if (cache.size() >= maxSize) {
            Iterator<String> it = cache.keySet().iterator();
            if (it.hasNext()) {
                cache.remove(it.next());
            }
        }

        cache.put(domain, new DNSEntry(domain, ip, 5));

        System.out.println(domain + " → Cache MISS → " + ip);
        return ip;
    }


    private String queryUpstreamDNS(String domain) {
        Random rand = new Random();
        return "172.217.14." + rand.nextInt(255);
    }


    public void cleanExpired() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            if (it.next().getValue().isExpired()) {
                it.remove();
            }
        }
    }


    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache();

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(6000); // simulate TTL expiry

        dns.resolve("google.com");

        dns.getCacheStats();
    }
}