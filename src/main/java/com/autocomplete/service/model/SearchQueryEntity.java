package com.autocomplete.service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_query")
public class SearchQueryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String query;

    private int frequency;

    private LocalDateTime lastSearched;

    public Long getId() { return id;}

    public String getQuery() { return query;}
    public void setQuery(String query) {this.query = query;}

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency){ this.frequency = frequency;}

    public LocalDateTime getLastSearched() { return lastSearched; }
    public void setLastSearched(LocalDateTime lastSearched){
        this.lastSearched = lastSearched;
    }

}
