package com.autocomplete.service.repository;

import com.autocomplete.service.model.SearchQueryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchQueryRepository extends JpaRepository<SearchQueryEntity, Long>{
    Optional<SearchQueryEntity> findByQuery(String query);
}
