package projectiondemo.rest;

import projectiondemo.domain.Reading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Cepro, 2017-03-25
 */
@Slf4j
@Component
@RepositoryEventHandler(Reading.class)
public class ReadingEventHandler {
    
    @HandleAfterCreate
    @HandleAfterSave
    @HandleAfterDelete
    @CacheEvict(value = {"bookRating", "authorRating"}, allEntries = true)
    public void cacheEvict(Reading reading) {
        LOG.info("<<< Caches 'bookRating' and 'authorRating' evicted >>>");
    }
}
