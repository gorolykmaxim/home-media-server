package com.gorolykmaxim.homemediaapp.qbittorrent;

import com.gorolykmaxim.homemediaapp.common.keyvalue.KeyValue;
import com.gorolykmaxim.homemediaapp.common.keyvalue.KeyValueRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class QbittorrentAuthorization {

    private RestTemplate restTemplate;
    private String username, password;
    private URI baseUri;
    private final String sidKeyName, sidCookie;
    private KeyValueRepository keyValueRepository;

    public QbittorrentAuthorization(RestTemplate restTemplate, String username, String password, URI baseUri,
                                    KeyValueRepository keyValueRepository) {
        this.restTemplate = restTemplate;
        this.username = username;
        this.password = password;
        this.baseUri = baseUri;
        this.sidKeyName = "qbittorrent-sid";
        this.sidCookie = "SID";
        this.keyValueRepository = keyValueRepository;
    }

    public void renew() throws AuthorizationRenewalError {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("username", username);
            body.add("password", password);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<String> response = restTemplate.postForEntity(baseUri.resolve("/login"), request, String.class);
            HttpHeaders responseHeaders = response.getHeaders();
            String setCookieHeader = responseHeaders.getFirst(HttpHeaders.SET_COOKIE);
            if (setCookieHeader == null) {
                throw new NoCookieInResponseError(response);
            }
            // We are working with set-cookie v.1, so there could be only one cookie in such header.
            HttpCookie cookie = HttpCookie.parse(setCookieHeader).get(0);
            KeyValue keyValue = new KeyValue(sidKeyName, cookie.getValue());
            keyValueRepository.save(keyValue);
        } catch (RuntimeException e) {
            throw new AuthorizationRenewalError(username, password, baseUri, e);
        }
    }

    @Transactional
    public void applyTo(HttpHeaders httpHeaders) throws AuthorizationRenewalError {
        Optional<KeyValue> possibleSidCookie = keyValueRepository.findByKey(sidKeyName);
        if (possibleSidCookie.isPresent()) {
            HttpCookie sidHttpCookie = new HttpCookie(sidCookie, possibleSidCookie.get().getValue());
            sidHttpCookie.setVersion(0);
            httpHeaders.add(HttpHeaders.COOKIE, sidHttpCookie.toString());
        } else {
            renew();
            applyTo(httpHeaders);
        }
    }

    public static class AuthorizationRenewalError extends RuntimeException {
        public AuthorizationRenewalError(String username, String password, URI baseUri, Throwable cause) {
            super(String.format("Failed to renew authorization using username '%s', password '%s' and base URI '%s'. " +
                    "Reason: %s", username, password, baseUri, cause), cause);
        }
    }

    public static class NoCookieInResponseError extends IllegalArgumentException {
        public NoCookieInResponseError(ResponseEntity<String> response) {
            super(String.format("SID cookie was not found in response: %s", response));
        }
    }

}
