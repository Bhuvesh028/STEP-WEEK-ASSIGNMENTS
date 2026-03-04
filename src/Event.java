import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}class RealTimeAnalytics {


    private HashMap<String, Integer> pageViews = new HashMap<>();


    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();


    private HashMap<String, Integer> trafficSources = new HashMap<>();


    public void processEvent(Event event) {


        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);


        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }


    public void getDashboard() {

        System.out.println("Top Pages:");


        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int limit = Math.min(10, list.size());

        for (int i = 0; i < limit; i++) {

            String page = list.get(i).getKey();
            int views = list.get(i).getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((i + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": "
                    + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new Event("/sports/championship", "user_789", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_123", "direct"));
        analytics.processEvent(new Event("/sports/championship", "user_999", "google"));

        analytics.getDashboard();
    }
}