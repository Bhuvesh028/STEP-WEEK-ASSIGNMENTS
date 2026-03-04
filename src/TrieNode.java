import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord = false;
}

class AutocompleteSystem {

    private TrieNode root = new TrieNode();


    private HashMap<String, Integer> frequencyMap = new HashMap<>();


    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isWord = true;

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }


    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c))
                return new ArrayList<>();
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, prefix, results);


        results.sort((a, b) ->
                frequencyMap.get(b) - frequencyMap.get(a));

        return results.subList(0, Math.min(10, results.size()));
    }


    private void dfs(TrieNode node, String word,
                     List<String> results) {

        if (node.isWord)
            results.add(word);

        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), word + c, results);
        }
    }


    public void updateFrequency(String query) {
        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java tutorial");

        List<String> suggestions = system.search("jav");

        System.out.println("Suggestions:");

        for (String s : suggestions) {
            System.out.println(s + " (" +
                    system.frequencyMap.get(s) +
                    " searches)");
        }
    }
}