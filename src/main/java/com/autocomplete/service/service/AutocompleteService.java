package com.autocomplete.service.service;

import com.autocomplete.service.model.SearchQueryEntity;
import com.autocomplete.service.repository.SearchQueryRepository;
import com.autocomplete.service.trie.Trie;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AutocompleteService {

    private final Trie trie = new Trie();
    private final SearchQueryRepository repository;

    public AutocompleteService(SearchQueryRepository repository){
        this.repository = repository;
    }

    @PostConstruct
    public void loadFromDatabase(){
        repository.findAll().forEach(entity -> trie.insert(entity.getQuery(), entity.getFrequency()));
    }

    public List<String> getSuggestions(String prefix, int limit){
        return trie.search(prefix.toLowerCase(), limit);
    }

    public void recordSearch(String query){
        SearchQueryEntity entity = repository
                .findByQuery(query)
                .orElseGet(()->{
                    SearchQueryEntity e = new SearchQueryEntity();
                    e.setQuery(query);
                    e.setFrequency(0);
                    return e;
                });
        entity.setFrequency(entity.getFrequency() + 1);
        entity.setLastSearched(LocalDateTime.now());

        repository.save(entity);
        trie.insert(query, 1);
    }
}
