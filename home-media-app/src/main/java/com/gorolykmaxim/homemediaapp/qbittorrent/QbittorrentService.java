package com.gorolykmaxim.homemediaapp.qbittorrent;

import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentService;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrent;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrentRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QbittorrentService implements TorrentService, DownloadingTorrentRepository {

    private RestTemplate restTemplate;
    private QbittorrentAuthorization authorization;
    private QbittorrentFactory factory;
    private URI baseUri;

    public QbittorrentService(RestTemplate restTemplate, QbittorrentAuthorization authorization,
                              QbittorrentFactory factory, URI baseUri) {
        this.restTemplate = restTemplate;
        this.authorization = authorization;
        this.factory = factory;
        this.baseUri = baseUri;
    }

    @Override
    public void downloadViaMagnetLink(String magnetLink, String downloadFolder) throws DownloadTorrentError {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            authorization.applyTo(httpHeaders);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("urls", magnetLink);
            body.add("savepath", downloadFolder);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
            restTemplate.postForLocation(baseUri.resolve("/command/download"), request);
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            downloadViaMagnetLink(magnetLink, downloadFolder);
        } catch (RuntimeException e) {
            throw new DownloadTorrentError(baseUri, magnetLink, downloadFolder, e);
        }
    }

    @Override
    public void deleteTorrentById(String id) throws DeleteTorrentError {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            authorization.applyTo(httpHeaders);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("hashes", id);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
            restTemplate.postForLocation(baseUri.resolve("/command/delete"), request);
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            deleteTorrentById(id);
        } catch (RuntimeException e) {
            throw new DeleteTorrentError(baseUri, id, e);
        }
    }

    @Override
    public List<DownloadingTorrent> findAll() throws GetTorrentsError {
        Map<String, String> uriParameters = new HashMap<>();
        uriParameters.put("sort", "progress");
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            authorization.applyTo(httpHeaders);
            HttpEntity request = new HttpEntity(httpHeaders);
            ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(baseUri.resolve("/query/torrents").toString(),
                    HttpMethod.GET, request, new ParameterizedTypeReference<List<Map<String, String>>>() {}, uriParameters);
            List<Map<String, String>> body = response.getBody();
            if (body == null) {
                throw new EmptyBodyError();
            }
            return body.stream().map(factory::create).collect(Collectors.toList());
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            return findAll();
        } catch (RuntimeException e) {
            throw new GetTorrentsError(baseUri, uriParameters, e);
        }
    }

    public static class EmptyBodyError extends RuntimeException {
        public EmptyBodyError() {
            super("Response body is empty");
        }
    }

    public static class GetTorrentsError extends RuntimeException {
        public GetTorrentsError(URI baseUri, Map<String, String> uriParameters, Throwable cause) {
            super(String.format("Failed to obtain list of torrents from qbittorrent via URI '%s' with parameters: '%s'. " +
                    "Reason: %s", baseUri, uriParameters, cause), cause);
        }
    }

    public static class DeleteTorrentError extends RuntimeException {
        public DeleteTorrentError(URI baseUri, String id, Throwable cause) {
            super(String.format("Failed to delete torrent with ID '%s' via URI '%s'. Reason: %s", id, baseUri, cause), cause);
        }
    }

    public static class DownloadTorrentError extends RuntimeException {
        public DownloadTorrentError(URI baseUri, String magnetLink, String downloadFolder, Throwable cause) {
            super(String.format("Failed to download torrent via magnet link '%s' to folder '%s' via URI '%s'. " +
                    "Reason: %s", magnetLink, downloadFolder, baseUri, cause), cause);
        }
    }

}
