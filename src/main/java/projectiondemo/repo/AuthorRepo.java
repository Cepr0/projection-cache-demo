package projectiondemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import projectiondemo.domain.Author;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface AuthorRepo extends JpaRepository<Author, Long> {
}
