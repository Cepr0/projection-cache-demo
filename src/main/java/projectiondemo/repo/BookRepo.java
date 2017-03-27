package projectiondemo.repo;

import projectiondemo.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface BookRepo extends JpaRepository<Book, Long> {

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
