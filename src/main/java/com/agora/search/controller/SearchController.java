package com.agora.search.controller;

import com.agora.search.dto.ProfileResponse;
import com.agora.search.service.SearchService;
import com.agora.user.dto.UpdateLocationRequest;
import com.agora.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final UserService userService;

    @PostMapping("/update-location")
    public ResponseEntity<Void> updateLocation(@RequestBody @Valid UpdateLocationRequest locationRequest) {
        userService.updateUserLocation(locationRequest);
        return ResponseEntity.ok().build();
    }
 
    @GetMapping("/recommendations")
    public ResponseEntity<List<ProfileResponse>> getRecommendations() {
        List<ProfileResponse> recommendations = searchService.recommendProfiles();
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommendations/by-relevance")
    public ResponseEntity<List<ProfileResponse>> getRecommendationsByRelevance() {
        List<ProfileResponse> recommendations = searchService.getRecommendedByRelevance();
        return ResponseEntity.ok(recommendations);
    }
}
