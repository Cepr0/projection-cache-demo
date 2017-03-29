package projectiondemo.repo;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import projectiondemo.domain.Book;

import java.util.List;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface BookRepo extends JpaRepository<Book, Long> {

    @RestResource(path = "topRating", rel = "topRating")
    @Query("select new Book(b.id, b.title, b.isbn, avg(r.rating), a.id, a.name, p.id, p.name) from Reading r join r.book b join r.book.author a join r.book.publisher p group by b order by avg(r.rating) desc, b.title asc")
    Page<Book> topRating(Pageable pageable);
    
    @RestResource(path = "topReadings", rel = "topReadings")
    @Query("select new Book(b.id, b.title, b.isbn, count(r), a.id, a.name, p.id, p.name) from Reading r join r.book b join r.book.author a join r.book.publisher p group by b order by count(r) desc, b.title asc")
    Page<Book> topReadings(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "publisher"})
    @RestResource(path = "byAuthor", rel = "byAuthor")
    Page<Book> findBooksByAuthorNameIgnoreCaseContaining(@Param("name") String authorName, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "publisher"})
    @RestResource(path = "byPublisher", rel = "byPublisher")
    Page<Book> findBooksByPublisherNameIgnoreCaseContaining(@Param("name") String publisherName, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author", "publisher"})
    List<Book> findAll();
    
    @Override
    @EntityGraph(attributePaths = {"author", "publisher"})
    Page<Book> findAll(Pageable pageable);
}
