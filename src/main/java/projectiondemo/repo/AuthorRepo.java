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
    
    // https://github.com/spring-projects/spring-data-jpa/blob/master/src/test/java/org/springframework/data/jpa/repository/sample/UserRepository.java
    
    @RestResource(path = "topRating", rel = "topRating")
    @Query(value = "select avg(r.rating) as rating, a as author from Reading r join r.book.author a group by r.book.author order by rating desc, a.name asc",
            countQuery = "select count(a.id) from Author a")
    Page<Author> topRating(Pageable pageable);

    @RestResource(path = "topReadings", rel = "topReadings")
    @Query(value = "select new Author(a.id, a.name, count(r)) from Reading r join r.book.author a group by r.book.author order by count(r) desc, a.name asc",
            countQuery = "select count(a.id) from Author a")
    Page<Author> topReadings(Pageable pageable);
}
