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
    
    @RestResource(path = "byAuthorName", rel = "byAuthorName")
    List<Book> findBooksByAuthorNameIgnoreCaseContaining(@Param("name") String authorName);
    
    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();
    
    @Override
    @EntityGraph(attributePaths = "author")
    Page<Book> findAll(Pageable pageable);
}
