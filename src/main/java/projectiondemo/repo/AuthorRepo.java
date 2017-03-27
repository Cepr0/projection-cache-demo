package projectiondemo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import projectiondemo.domain.Author;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface AuthorRepo extends JpaRepository<Author, Long> {
    
    @RestResource(path = "topRating", rel = "topRating")
    @Query("select new Author(a.id, a.name, avg(r.rating)) from Reading r join r.book.author a group by r.book.author order by avg(r.rating) desc")
    Page<Author> topRating(Pageable pageable);
    
    @RestResource(path = "topReadings", rel = "topReadings")
    @Query("select new Author(a.id, a.name, count(r)) from Reading r join r.book.author a group by r.book.author order by count(r) desc")
    Page<Author> topReadings(Pageable pageable);
}
