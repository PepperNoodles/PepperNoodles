package com.peppernoodles.tag.repository;

import com.peppernoodles.tag.domain.FoodTag;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {

    Optional<FoodTag> findByName(String name);

    List<FoodTag> findByIdIn(Collection<Long> ids);

    List<FoodTag> findAllByOrderByNameAsc();

    /**
     * Prefix/substring search backing the tag autocomplete. Replaces the legacy
     * jQuery Typeahead + Bloodhound endpoint, which loaded every tag into the
     * browser on each page view.
     */
    @Query("select t from FoodTag t where lower(t.name) like lower(concat('%', :q, '%')) order by t.name")
    List<FoodTag> search(@Param("q") String query);
}
