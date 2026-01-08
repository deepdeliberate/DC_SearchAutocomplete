package com.autocomplete.service.service;

import com.autocomplete.service.model.SearchQueryEntity;
import com.autocomplete.service.repository.SearchQueryRepository;
import com.autocomplete.service.trie.Trie;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AutocompleteService {

    private final Trie trie = new Trie();
    private final SearchQueryRepository repository;
    private final RedisTemplate<String, List<String>> redisTemplate;

    public AutocompleteService(SearchQueryRepository repository,
                               RedisTemplate<String, List<String>> redisTemplate){
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void loadFromDatabase(){
        List<SearchQueryEntity> allQueries = repository.findAll();
        allQueries.forEach(q->trie.insert(q.getQuery(), q.getFrequency()));

        System.out.println("Trie rebuilt from databse with " + allQueries.size() + " queries.");
    }

    public List<String> getSuggestions(String prefix, int limit) {
        String key = "autocomplete:" + prefix + ":" + limit;

        // Try Redis cache
        List<String> cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        // Cache Miss -> Search in Trie
        List<String> result = trie.search(prefix.toLowerCase(), limit);

        // Store result in Redis
        redisTemplate.opsForValue().set(key, result, Duration.ofMinutes(10));

        return result;
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
