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
    
    /**
     * An attempt to get the book list sorted by book rating and expose it with Spring Data REST.
     * <p>But if we call it 'GET http://localhost:8080/api/books/search/topRating'
     * it raises an error: 'PersistentEntity must not be null!'
     */
    @RestResource(path = "topRating", rel = "topRating")
    @Transactional(readOnly = true)
    @Query(value = "select r.book as book, r.book.author as author, r.book.publisher as publisher, avg(r.rating) as rating, count(r) as readings from Reading r group by r.book order by rating desc, r.book.title asc",
            countQuery = "select count(b) from Book b")
    Page<Book.BookRatings> topRating(Pageable pageable);
    
    /**
     * An attempt to get the book list sorted by book number of reading and expose it with Spring Data REST.
     * <p>But if we call it 'GET http://localhost:8080/api/books/search/topReadings'
     * it raises an error: 'PersistentEntity must not be null!'
     */
    @RestResource(path = "topReadings", rel = "topReadings")
    @Transactional(readOnly = true)
    @Query(value = "select r.book as book, r.book.author as author, r.book.publisher as publisher, avg(r.rating) as rating, count(r) as readings from Reading r group by r.book order by readings desc, r.book.title asc",
            countQuery = "select count(b) from Book b")
    Page<Book.BookRatings> topReadings(Pageable pageable);
    
    /**
     * Example of 'search' method - find books by author name
     * <p>@EntityGraph is used to avoid 1+N queries
     */
    @EntityGraph(attributePaths = {"author", "publisher"})
    @RestResource(path = "byAuthor", rel = "byAuthor")
    Page<Book> findBooksByAuthorNameIgnoreCaseContaining(@Param("name") String authorName, Pageable pageable);
    
    /**
     * Example of 'search' method - find books by publisher name
     * <p>@EntityGraph is used to avoid 1+N queries
     */
    @EntityGraph(attributePaths = {"author", "publisher"})
    @RestResource(path = "byPublisher", rel = "byPublisher")
    Page<Book> findBooksByPublisherNameIgnoreCaseContaining(@Param("name") String publisherName, Pageable pageable);
    
    /**
     * We need to override this method to force it make only one query (not 1+N queries) with help of @EntityGraph
     */
    @Override
    @EntityGraph(attributePaths = {"author", "publisher"})
    List<Book> findAll();
    
    /**
     * We need to override this method to force it make only one query (not 1+N queries) with help of @EntityGraph
     */
    @Override
    @EntityGraph(attributePaths = {"author", "publisher"})
    Page<Book> findAll(Pageable pageable);
    
    /**
     * Example of using Projections as output parameter in the repository method
     * <p>But than we call it as 'GET http://localhost:8080/api/books/search/withAuthor'
     * it raises an error: 'PersistentEntity must not be null!'
     */
    @RestResource(path = "withAuthor", rel = "withAuthor")
    @Query("select b.title as title, b.isbn as isbn, a as author from Book b join b.author a")
    Page<Book.BookAuthor> getBooksWithAuthor(Pageable pageable);
}
