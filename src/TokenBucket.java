import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    int refillRate; // tokens added per second
    long lastRefillTime;

    TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }


    private void refill() {
        long now = System.currentTimeMillis();
        long seconds = (now - lastRefillTime) / 1000;

        if (seconds > 0) {
            int tokensToAdd = (int) (seconds * refillRate);
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }


    synchronized boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    int getRemainingTokens() {
        return tokens;
    }
}

class RateLimiter {


    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int limit = 1000;
    private int refillRate = 1000 / 3600; // tokens per second

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(limit, refillRate));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            System.out.println("Allowed (" +
                    bucket.getRemainingTokens() +
                    " requests remaining)");
            return true;
        } else {
            System.out.println("Denied (Rate limit exceeded)");
            return false;
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made yet");
            return;
        }

        int used = limit - bucket.getRemainingTokens();

        System.out.println("Used: " + used +
                ", Limit: " + limit +
                ", Remaining: " + bucket.getRemainingTokens());
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");

        limiter.getRateLimitStatus("abc123");
    }
}