package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import com.gorolykmaxim.thumbnailsearch.common.PersistableUri;

import java.sql.Date;
import java.time.LocalDate;

public class CachedImageFactory {
    public boolean isUriSupported(String uri) {
        return uri != null && uri.length() < PersistableUri.MAXIMAL_URI_LENGTH;
    }
    public CachedImage create(String uri, String searchTerm) {
        return new CachedImage(uri, searchTerm, Date.valueOf(LocalDate.now()));
    }
}