package projectiondemo.repo;

import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Reading;
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
public interface ReadingRepo extends JpaRepository<Reading, Long> {
    
    @Cacheable(value = "bookRating", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating from Reading r where r.book = ?1 group by r.book")
    Float getBookRating(Book book);
    
    @Cacheable(value = "authorRating", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating from Reading r where r.book.author = ?1 group by r.book.author")
    Float getAuthorRating(Author author);
}
