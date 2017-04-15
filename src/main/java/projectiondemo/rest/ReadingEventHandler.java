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
import projectiondemo.ProjectionDemo;
import projectiondemo.domain.Book;
import projectiondemo.domain.Reading;
import projectiondemo.repo.ReadingRepo;

/**
 * Catches {@link ReadingRepo} events
 * @author Cepro, 2017-03-25
 */
@Slf4j
@RequiredArgsConstructor
@Component
@RepositoryEventHandler(Reading.class)
public class ReadingEventHandler {

    private final @NonNull CacheManager cacheManager;
    
    /**
     * Catch 'create', 'save' and 'delete' {@link ReadingRepo} events to evict 'ratings' caches
     * See {@link ProjectionDemo#cacheManager} and {@link ReadingRepo}
     */
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
