package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class PrefixTree<T> {
    private final TreeNode root;

    public PrefixTree() {
        this.root = new TreeNode('*', null);
    }

    public void insert(String word, T value) {
        insertWord(word, value);
    }

    public void insert(String word) {
        insertWord(word, null);
    }

    private void insertWord(String word, T value) {
        TreeNode currentNode = root;
        for (int i = word.length() - 1; i >= 0; i--) {
            char symbol = word.charAt(i);
            if (!currentNode.getChildren().containsKey(symbol)) {
                currentNode.getChildren().put(symbol,new TreeNode(symbol, value));
                currentNode = currentNode.getNode(symbol);
                if (i == 0 && symbol == word.charAt(0)) {
                    currentNode.setEndOfWord(true);
                }
            } else
                currentNode = currentNode.getNode(symbol);
        }
    }


    public boolean wordSearch(String value) {
        String search = new StringBuilder(value).reverse().toString();
        TreeNode currentNode = root;
        for (int i = value.length() - 1; i >= 0; i--) {
            if (currentNode.getChildren().containsKey(value.charAt(i)))
                currentNode = currentNode.getNode(value.charAt(i));
            else
                return false;

            if (value.charAt(i) == value.charAt(0) && i == 0) {
                return currentNode.isEndOfWord();
            }
        }
        return false;
    }

    public List<String> prefixSearch(String value) {
        String search = new StringBuilder(value).reverse().toString();
        TreeNode currentNode = root;
        for (char symbol : search.toCharArray()) {
            if (currentNode.getChildren().containsKey(symbol))
                currentNode = currentNode.getNode(symbol);
            else
                return Collections.emptyList();
        }
        List<String> result = discoverWords(currentNode, search, new ArrayList<>());


        return result.stream()
                .map(it -> new StringBuilder(it).reverse().toString())
                .collect(Collectors.toList());
    }

    public boolean hasPrefix(String value) {
        String search = new StringBuilder(value).reverse().toString();
        TreeNode currentNode = root;
        for (char symbol : search.toCharArray()) {
            if (currentNode.getChildren().containsKey(symbol))
                currentNode = currentNode.getNode(symbol);
            else
                return false;
        }
        return true;
    }

    public void delete(String value) {
        String word = new StringBuilder(value).reverse().toString();
        delete(root, word, 0);
    }

    private boolean delete(TreeNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TreeNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndOfWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }

    private List<String> discoverWords(TreeNode node, String prefix, List<String> words) {
        for (Map.Entry<Character, PrefixTree<T>.TreeNode> entry : node.getChildren().entrySet()) {
            if (entry.getValue().isEndOfWord())
                words.add(prefix + entry.getKey());
            discoverWords(entry.getValue(), prefix + entry.getKey(), words);
        }
        return words;
    }

    public void reset() {
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode currentNode = queue.remove();
            queue.addAll(currentNode.getChildren().values());
            currentNode.setVisited(false);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public class TreeNode {
        private final HashMap<Character, TreeNode> children;
        private T value;
        private Character key;
        private boolean endOfWord = false;
        private boolean isVisited = false;

        public Character getKey() {
            return key;
        }

        public boolean isVisited() {
            return isVisited;
        }

        public void setVisited(boolean visited) {
            isVisited = visited;
        }

        public T getValue() {
            return value;
        }

        public TreeNode() {
            this.children = new HashMap<>();
        }

        public TreeNode(Character key, T value) {
            this.key = key;
            this.value = value;
            this.children = new HashMap<>();
        }

        public TreeNode getNode(char symbol) {
            return children.get(symbol);
        }

        public void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

        public Map<Character,TreeNode> getChildren() {
            return children;
        }

        public boolean isEndOfWord() {
            return endOfWord;
        }

    }
}
