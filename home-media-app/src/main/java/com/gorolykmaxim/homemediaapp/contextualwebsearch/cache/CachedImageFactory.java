package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import com.gorolykmaxim.homemediaapp.common.PersistableUri;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;

public class CachedImageFactory {
    public boolean isUriSupported(URI uri) {
        return uri != null && uri.toString().length() < PersistableUri.MAXIMAL_URI_LENGTH;
    }
    public CachedImage create(URI uri, String searchTerm) {
        return new CachedImage(uri, searchTerm, Date.valueOf(LocalDate.now()));
    }
}
