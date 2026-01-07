package com.autocomplete.service.trie;

import  java.util.*;

public class Trie {
    private final TrieNode root = new TrieNode();

    public void insert(String word, int frequency){
        TrieNode node = root;
        for(char ch: word.toCharArray()){
            node = node.children.computeIfAbsent(ch, c-> new TrieNode());
        }

        node.isEndofWord = true;
        node.frequency += frequency;
        node.word = word;
    }

    public List<String> search(String prefix, int k){
        TrieNode node = root;

        for(char ch: prefix.toCharArray()){
            if(!node.children.containsKey(ch)){
                return Collections.emptyList();
            }
            node = node.children.get(ch);
        }

        // Collect all words from leaf nodes of this node.
        PriorityQueue<TrieNode> pq = new PriorityQueue<>(
                (a,b) -> b.frequency - a.frequency
        );

        dfs(node, pq);

        // Extract top K
        List<String> result = new ArrayList<>();
        while(!pq.isEmpty() && result.size() < k){
            result.add(pq.poll().word);
        }

        return result;
    }

    private void dfs(TrieNode node, PriorityQueue<TrieNode> pq) {
        if(node.isEndofWord){
            pq.offer(node);
        }

        for(TrieNode child: node.children.values()){
            dfs(child, pq);
        }
    }
}
