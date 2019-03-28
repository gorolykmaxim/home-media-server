package com.gorolykmaxim.homemediaapp.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class PersistableUri implements Serializable {
    @Transient
    public static final int MAXIMAL_URI_LENGTH = 2000;
    @Column(length = MAXIMAL_URI_LENGTH)
    private String uri;

    protected PersistableUri() {}

    public PersistableUri(URI uri) {
        this.uri = uri.toString();
    }

    public URI getUri() {
        return URI.create(uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistableUri that = (PersistableUri) o;
        return Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }
}
