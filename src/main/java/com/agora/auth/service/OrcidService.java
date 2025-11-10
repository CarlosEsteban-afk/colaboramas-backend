package com.agora.auth.service;

import com.agora.auth.dto.OrcidTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrcidService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${orcid.client-id}")
    private String orcidClientId;

    @Value("${orcid.client-secret}")
    private String orcidClientSecret;

    @Value("${orcid.redirect-uri}")
    private String orcidRedirectUri;

    public String getOrcidId(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", orcidClientId);
        body.add("client_secret", orcidClientSecret);
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", orcidRedirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        OrcidTokenResponse response = restTemplate.postForObject("https://sandbox.orcid.org/oauth/token", request, OrcidTokenResponse.class);

        if (response != null) {
            return response.getOrcid();
        }

        return null;
    }
}
