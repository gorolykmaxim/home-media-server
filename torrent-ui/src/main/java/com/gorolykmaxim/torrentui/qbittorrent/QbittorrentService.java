package com.gorolykmaxim.torrentui.qbittorrent;

import com.gorolykmaxim.torrentui.common.EmptyBodyError;
import com.gorolykmaxim.torrentui.model.DownloadingTorrent;
import com.gorolykmaxim.torrentui.model.TorrentService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QbittorrentService implements TorrentService {

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
    public List<DownloadingTorrent> find(Map<String, String> parameters) {
        URI uri = baseUri.resolve("/api/v2/torrents/info");
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            authorization.applyTo(httpHeaders);
            HttpEntity request = new HttpEntity(httpHeaders);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
            for (Map.Entry<String, String> parameter: parameters.entrySet()) {
                builder.queryParam(parameter.getKey(), parameter.getValue());
            }
            ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(builder.build().toUri(),
                    HttpMethod.GET, request, new ParameterizedTypeReference<List<Map<String, String>>>() {});
            List<Map<String, String>> body = response.getBody();
            if (body == null) {
                throw new EmptyBodyError();
            }
            return body.stream().map(factory::create).collect(Collectors.toList());
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            return find(parameters);
        } catch (RuntimeException e) {
            throw new GetTorrentsError(uri, parameters, e);
        }
    }

    @Override
    public void downloadViaMagnetLink(String magnetLink, String downloadFolder) throws DownloadTorrentError {
        URI uri = baseUri.resolve("/api/v2/torrents/add");
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            authorization.applyTo(httpHeaders);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("urls", magnetLink);
            body.add("savepath", downloadFolder);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
            restTemplate.postForLocation(uri, request);
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            downloadViaMagnetLink(magnetLink, downloadFolder);
        } catch (RuntimeException e) {
            throw new DownloadTorrentError(uri, magnetLink, downloadFolder, e);
        }
    }

    @Override
    public void deleteTorrentById(String id, boolean deleteData) throws DeleteTorrentError {
        URI uri = UriComponentsBuilder.fromUri(baseUri.resolve("/api/v2/torrents/delete"))
                .queryParam("hashes", id)
                .queryParam("deleteFiles", deleteData)
                .build().toUri();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            authorization.applyTo(httpHeaders);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
            restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);
        } catch (HttpClientErrorException.Forbidden e) {
            // Possibly existing authorization has expired. Try to renew.
            authorization.renew();
            deleteTorrentById(id, deleteData);
        } catch (RuntimeException e) {
            throw new DeleteTorrentError(uri, id, e);
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
