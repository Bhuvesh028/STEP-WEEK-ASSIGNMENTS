import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class MultiLevelCache {

    // L1 Cache (LRU using LinkedHashMap)
    private LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 10000;
                }
            };


    private HashMap<String, VideoData> L2 = new HashMap<>();


    private HashMap<String, VideoData> L3 = new HashMap<>();


    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;


    public VideoData getVideo(String videoId) {


        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }


        if (L2.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L1 MISS → L2 HIT (5ms)");

            VideoData video = L2.get(videoId);
            L1.put(videoId, video); // promote to L1

            return video;
        }


        if (L3.containsKey(videoId)) {
            l3Hits++;
            System.out.println("L1 MISS → L2 MISS → L3 HIT (150ms)");

            VideoData video = L3.get(videoId);
            L2.put(videoId, video); // add to L2

            return video;
        }

        System.out.println("Video not found");
        return null;
    }


    public void addVideo(String videoId, String content) {
        L3.put(videoId, new VideoData(videoId, content));
    }


    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\nCache Statistics:");

        if (total == 0) total = 1;

        System.out.println("L1 Hit Rate: " + (l1Hits * 100 / total) + "%");
        System.out.println("L2 Hit Rate: " + (l2Hits * 100 / total) + "%");
        System.out.println("L3 Hit Rate: " + (l3Hits * 100 / total) + "%");

        System.out.println("Total Requests: " + total);
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();


        cache.addVideo("video_123", "Movie A");
        cache.addVideo("video_999", "Movie B");

        cache.getVideo("video_123"); // L3 → L2
        cache.getVideo("video_123"); // L2 → L1
        cache.getVideo("video_123"); // L1 hit

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}

