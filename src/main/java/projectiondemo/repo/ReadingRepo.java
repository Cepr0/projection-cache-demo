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

/**
 * @author Cepro, 2017-03-24
 */
@SuppressWarnings({"SpringCacheAnnotationsOnInterfaceInspection", "SpringDataRepositoryMethodReturnTypeInspection"})
@RepositoryRestResource
public interface ReadingRepo extends JpaRepository<Reading, Long> {
    
    @Cacheable(value = "bookRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book = ?1")
    Reading.Ratings getBookRatings(Book book);

    @Cacheable(value = "authorRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book.author = ?1")
    Reading.Ratings getAuthorRatings(Author author);

    @Cacheable(value = "publisherRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book.publisher = ?1")
    Reading.Ratings getPublisherRatings(Publisher publisher);
}
