package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import java.sql.Date;
import java.time.LocalDate;

public class OlderThanTodayStrategy implements CleanupStrategy {
    @Override
    public void clean(CachedImageRepository repository) {
        repository.deleteAllByCreationDateBefore(Date.valueOf(LocalDate.now()));
    }
}
