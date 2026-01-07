package com.autocomplete.service.service;

import com.autocomplete.service.trie.Trie;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutocompleteService {

    private final Trie trie = new Trie();

    @PostConstruct
    public void init(){
        // Example Data
        trie.insert("java", 100);
        trie.insert("javascript", 90);
        trie.insert("java tutorial", 80);
        trie.insert("java spring", 70);
        trie.insert("python", 95);
    }

    public List<String> getSuggestions(String prefix, int limit){
        return trie.search(prefix.toLowerCase(), limit);
    }
}
