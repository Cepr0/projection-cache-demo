package projectiondemo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import projectiondemo.domain.Publisher;

/**
 * @author Cepro, 2017-03-27
 */
@RepositoryRestResource
public interface PublisherRepo extends JpaRepository<Publisher, Long> {

    @RestResource(path = "topRating", rel = "topRating")
    @Query("select new Publisher(p.id, p.name, avg(r.rating)) from Reading r join r.book.publisher p group by p order by avg(r.rating) desc, p.name asc")
    Page<Publisher> topRating(Pageable pageable);
    
    @RestResource(path = "topReadings", rel = "topReadings")
    @Query("select new Publisher(p.id, p.name, count(r)) from Reading r join r.book.publisher p group by p order by count(r) desc, p.name asc")
    Page<Publisher> topReadings(Pageable pageable);
}
