package com.autocomplete.service.controller;


import com.autocomplete.service.service.AutocompleteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autocomplete")
public class AutocompleteController {
    private final AutocompleteService autocompleteService;

    public AutocompleteController(AutocompleteService autocompleteService){
        this.autocompleteService = autocompleteService;
    }

    @GetMapping
    public List<String> autocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return autocompleteService.getSuggestions(q, limit);
    }

    @PostMapping("/search")
    public void search(@RequestParam String q){
        autocompleteService.recordSearch(q.toLowerCase());
    }

}
