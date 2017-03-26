package projectiondemo.repo;

import projectiondemo.domain.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Cepro, 2017-03-24
 */
@RepositoryRestResource
public interface ReaderRepo extends JpaRepository<Reader, Long> {
}