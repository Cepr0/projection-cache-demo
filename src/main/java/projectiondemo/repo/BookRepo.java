package projectiondemo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import projectiondemo.domain.Book;

import java.util.List;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface BookRepo extends JpaRepository<Book, Long> {

    // https://github.com/spring-projects/spring-data-jpa/blob/master/src/test/java/org/springframework/data/jpa/repository/sample/UserRepository.java
    
    @RestResource(path = "topRating", rel = "topRating")
    @Transactional(readOnly = true)
    @Query(value = "select new Book(r.book.id, r.book.title, r.book.isbn, avg(r.rating), r.book.author.id, r.book.author.name, r.book.publisher.id, r.book.publisher.name) from Reading r group by r.book order by avg(r.rating) desc, r.book.title asc",
            countQuery = "select count(b) from Book b")
    Page<Book> topRating(Pageable pageable);
    
    @RestResource(path = "topReadings", rel = "topReadings")
    @Transactional(readOnly = true)
    @Query(value = "select new Book(r.book.id, r.book.title, r.book.isbn, count(r), r.book.author.id, r.book.author.name, r.book.publisher.id, r.book.publisher.name) from Reading r group by r.book order by count(r) desc, r.book.title asc",
            countQuery = "select count(b) from Book b")
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
