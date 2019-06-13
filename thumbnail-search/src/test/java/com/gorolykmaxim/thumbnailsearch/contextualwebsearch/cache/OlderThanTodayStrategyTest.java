package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;

public class OlderThanTodayStrategyTest {

    @Test
    public void clean() {
        CachedImageRepository repository = Mockito.mock(CachedImageRepository.class);
        CleanupStrategy strategy = new OlderThanTodayStrategy();
        Date expectedDate = Date.valueOf(LocalDate.now());
        strategy.clean(repository);
        Mockito.verify(repository).deleteAllByCreationDateBefore(expectedDate);
    }
}