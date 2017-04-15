package projectiondemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import projectiondemo.domain.Publisher;

/**
 * @author Cepro, 2017-03-27
 */
@RepositoryRestResource
public interface PublisherRepo extends JpaRepository<Publisher, Long> {
}
