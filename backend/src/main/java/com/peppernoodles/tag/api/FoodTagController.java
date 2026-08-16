package com.peppernoodles.tag.api;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.tag.repository.FoodTagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food-tags")
@Tag(name = "Food tags", description = "食物標籤")
public class FoodTagController {

    private final FoodTagRepository foodTags;

    public FoodTagController(FoodTagRepository foodTags) {
        this.foodTags = foodTags;
    }

    @GetMapping
    @Operation(
            summary = "列出或搜尋標籤",
            description = "Server-side search replacing the legacy Typeahead/Bloodhound endpoint, "
                    + "which shipped the entire tag table to the browser on every page load.")
    public List<TagSummary> list(@RequestParam(required = false) String q) {
        var tags = (q == null || q.isBlank()) ? foodTags.findAllByOrderByNameAsc() : foodTags.search(q.trim());
        return tags.stream().map(TagSummary::from).toList();
    }
}
