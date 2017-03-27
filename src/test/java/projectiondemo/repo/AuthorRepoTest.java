package projectiondemo.repo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import projectiondemo.BaseTest;
import projectiondemo.domain.Author;

/**
 * @author Cepro, 2017-03-28
 */
public class AuthorRepoTest extends BaseTest {
    
    @Autowired
    private AuthorRepo authorRepo;
    
    @Test
    public void topRating() throws Exception {
    
        Page<Author> authors = authorRepo.topRating(new PageRequest(0, 20));
        authors.getContent().forEach(System.out::println);
    }
}