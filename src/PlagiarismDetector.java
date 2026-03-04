import java.util.*;

public class PlagiarismDetector {


    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    private int N = 5; // using 5-grams


    private List<String> generateNGrams(String text) {

        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }


    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        System.out.println(docId + " indexed with " + ngrams.size() + " n-grams");
    }


    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String existingDoc : ngramIndex.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "data structures and algorithms are important for computer science students";
        String essay2 = "data structures and algorithms are very important in computer science education";
        String essay3 = "machine learning models require large datasets for training";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);

        System.out.println("\nAnalyzing new document...\n");

        detector.analyzeDocument("essay_123.txt", essay1);
    }
}