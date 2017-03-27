package projectiondemo.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import projectiondemo.domain.Book;
import projectiondemo.domain.Reading;

/**
 * @author Cepro, 2017-03-25
 */
@Slf4j
@RequiredArgsConstructor
@Component
@RepositoryEventHandler(Reading.class)
public class ReadingEventHandler {

    private final @NonNull CacheManager cacheManager;

    @HandleAfterCreate
    @HandleAfterSave
    @HandleAfterDelete
    public void evictCaches(Reading reading) {
        Book book = reading.getBook();
        cacheManager.getCache("bookRatings").evict(book.getId());
        cacheManager.getCache("authorRatings").evict(book.getAuthor().getId());
        cacheManager.getCache("publisherRatings").evict(book.getPublisher().getId());

        LOG.info("<<< Ratings caches evicted >>>");
    }
}
