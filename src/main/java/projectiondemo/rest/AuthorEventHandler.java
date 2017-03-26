package projectiondemo.rest;

import projectiondemo.domain.Author;
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
@RepositoryEventHandler(Author.class)
public class AuthorEventHandler {
    
    @HandleAfterCreate
    @HandleAfterSave
    @HandleAfterDelete
    @CacheEvict(value = "bookAuthor", allEntries = true)
    public void cacheEvict(Author author) {
        LOG.info("<<< Cache 'bookAuthor' evicted >>>");
    }
}
