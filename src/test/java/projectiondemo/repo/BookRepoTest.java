package projectiondemo.repo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectiondemo.BaseTest;
import projectiondemo.domain.Book;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Cepro, 2017-03-26
 */
public class BookRepoTest extends BaseTest {
    
    @Autowired
    private BookRepo bookRepo;
    
    @Test
    public void getOne() throws Exception {
        Book book = bookRepo.getOne(1L);
        assertThat(book.getAuthor().getName(), is("Author1"));
    }
    
    @Test
    public void findAll() throws Exception {
        List<Book> books = bookRepo.findAll();
        assertThat(books, hasSize(6));
    }
}