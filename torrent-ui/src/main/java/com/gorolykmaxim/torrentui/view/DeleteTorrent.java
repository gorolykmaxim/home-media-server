package com.gorolykmaxim.torrentui.view;

import java.util.Objects;

public class DeleteTorrent {
    private String id;
    private boolean deleteData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleteData() {
        return deleteData;
    }

    public void setDeleteData(boolean deleteData) {
        this.deleteData = deleteData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteTorrent that = (DeleteTorrent) o;
        return deleteData == that.deleteData &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleteData);
    }
}
