package com.autocomplete.service.trie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndofWord;
    int frequency;
    // Storing word at leaf node (faster response)
    String word;
}
