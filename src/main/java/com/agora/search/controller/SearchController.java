package com.agora.search.controller;

import com.agora.search.dto.ProfileResponse;
import com.agora.search.service.SearchService;
import com.agora.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private SearchService searchService;
 
    @GetMapping("/recommendations")
    public ResponseEntity<List<ProfileResponse>> getRecommendations() {
        List<ProfileResponse> recommendations = searchService.recommendProfiles();
        return ResponseEntity.ok(recommendations);
    }
}
