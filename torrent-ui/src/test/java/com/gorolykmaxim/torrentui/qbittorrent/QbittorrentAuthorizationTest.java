package com.gorolykmaxim.torrentui.qbittorrent;

import com.gorolykmaxim.torrentui.common.keyvalue.KeyValue;
import com.gorolykmaxim.torrentui.common.keyvalue.KeyValueRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

public class QbittorrentAuthorizationTest {

    private RestTemplate restTemplate;
    private KeyValueRepository repository;
    private QbittorrentAuthorization authorization;
    private String username, password, sidKeyName, sidCookie, sidValue;
    private URI uri;

    @Before
    public void setUp() throws Exception {
        restTemplate = Mockito.mock(RestTemplate.class);
        repository = Mockito.mock(KeyValueRepository.class);
        username = "admin";
        password = "adminadmin";
        sidKeyName = "qbittorrent-sid";
        sidCookie = "SID";
        uri = URI.create("http://localhost:8080");
        authorization = new QbittorrentAuthorization(restTemplate, username, password, uri, repository);
        uri = uri.resolve("/login");
        sidValue = "3133exaykXDX0mUQRDukIr9YUi0EchFY";
    }

    @Test(expected = QbittorrentAuthorization.AuthorizationRenewalError.class)
    public void failToRenewSinceThereAreNoCookies() {
        Mockito.when(restTemplate.postForEntity(uri, createRequest(), String.class)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        authorization.renew();
    }

    @Test(expected = QbittorrentAuthorization.AuthorizationRenewalError.class)
    public void failToRenewSinceThereIsNoSIDCookie() {
        Mockito.when(restTemplate.postForEntity(uri, createRequest(), String.class)).thenReturn(createResponse(false));
        authorization.renew();
    }

    @Test
    public void renew() {
        Mockito.when(restTemplate.postForEntity(uri, createRequest(), String.class)).thenReturn(createResponse(true));
        authorization.renew();
        KeyValue keyValue = new KeyValue(sidKeyName, sidValue);
        Mockito.verify(repository).save(keyValue);
    }

    @Test
    public void applySavedSID() {
        KeyValue keyValue = new KeyValue(sidKeyName, sidValue);
        Mockito.when(repository.findByKey(sidKeyName)).thenReturn(Optional.of(keyValue));
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        authorization.applyTo(httpHeaders);
        Mockito.verify(httpHeaders).add(HttpHeaders.COOKIE, String.format("%s=%s", sidCookie, sidValue));
    }

    @Test
    public void renewAuthorizationAndApplySID() {
        KeyValue keyValue = new KeyValue(sidKeyName, sidValue);
        Mockito.when(repository.findByKey(sidKeyName)).thenAnswer(new Answer<Optional<KeyValue>>() {
            private boolean isFirstCall = true;
            @Override
            public Optional<KeyValue> answer(InvocationOnMock invocation) throws Throwable {
                if (isFirstCall) {
                    // First don't find a saved SID.
                    isFirstCall = false;
                    return Optional.empty();
                } else {
                    // Second, find recently saved SID.
                    return Optional.of(keyValue);
                }
            }
        });
        Mockito.when(restTemplate.postForEntity(uri, createRequest(), String.class)).thenReturn(createResponse(true));
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        authorization.applyTo(httpHeaders);
        Mockito.verify(repository).save(new KeyValue(sidKeyName, sidValue));
        Mockito.verify(httpHeaders).add(HttpHeaders.COOKIE, String.format("%s=%s", sidCookie, sidValue));
    }

    private HttpEntity<MultiValueMap<String, String>> createRequest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        return new HttpEntity<>(body, httpHeaders);
    }

    private ResponseEntity<String> createResponse(boolean hasCookie) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (hasCookie) {
            httpHeaders.add(HttpHeaders.SET_COOKIE, String.format("SID=%s; path=/", sidValue));
        }
        return new ResponseEntity<>("", httpHeaders, HttpStatus.OK);
    }

}
