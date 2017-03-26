package projectiondemo.repo;

import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Cepro, 2017-03-24
 */
@SuppressWarnings({"SpringCacheAnnotationsOnInterfaceInspection", "SpringDataRepositoryMethodReturnTypeInspection"})
@RepositoryRestResource
public interface AuthorRepo extends JpaRepository<Author, Long> {
    
    @Cacheable(value = "bookAuthor", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select a.name from Book b join b.author a where b = ?1")
    String getAuthorByBook(Book book);
    
    @Cacheable(value = "bookAuthor", key = "#a0")
    @RestResource(exported = false)
    @Query("select a.name from Author a where a.id = ?1")
    String getAuthorName(Long id);
}
