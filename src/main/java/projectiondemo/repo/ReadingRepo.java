package projectiondemo.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Publisher;
import projectiondemo.domain.Reading;
import projectiondemo.rest.ReadingEventHandler;

/**
 * @author Cepro, 2017-03-24
 */
@SuppressWarnings("SpringCacheAnnotationsOnInterfaceInspection")
@RepositoryRestResource
public interface ReadingRepo extends JpaRepository<Reading, Long> {
    
    /**
     * Helper method used in projection {@link Book.Ratings} to calculate rating and readings count for Books
     * <p>We are forced to cache their values to avoid 1+N queries when we call 'http://localhost:8080/api/books?projection=bookRating'
     * <p>(See {@link ReadingEventHandler} where the cache is evicted)
     */
    @Cacheable(value = "bookRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book = ?1")
    Reading.Ratings getBookRatings(Book book);
    
    /**
     * Helper method used in projection {@link Author.Ratings} to calculate rating and readings count for Authors
     * <p>We are forced to cache their values to avoid 1+N queries when we call 'http://localhost:8080/api/authors?projection=authorRating'
     * <p>(See {@link ReadingEventHandler} where the cache is evicted)
     */
    @Cacheable(value = "authorRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book.author = ?1")
    Reading.Ratings getAuthorRatings(Author author);
    
    /**
     * Helper method used in projection {@link Publisher.Ratings} to calculate rating and readings count for Publishers
     * <p>We are forced to cache their values to avoid 1+N queries when we call 'http://localhost:8080/api/publishers?projection=publisherRating'
     * <p>(See {@link ReadingEventHandler} where the cache is evicted)
     */
    @Cacheable(value = "publisherRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book.publisher = ?1")
    Reading.Ratings getPublisherRatings(Publisher publisher);
}
