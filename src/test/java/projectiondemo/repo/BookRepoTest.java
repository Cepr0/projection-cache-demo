package projectiondemo.repo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import projectiondemo.BaseTest;
import projectiondemo.domain.Book;
import projectiondemo.dto.BookWithRatings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Cepro, 2017-03-26
 */
public class BookRepoTest extends BaseTest {
    
    @Autowired
    private BookRepo bookRepo;
    
    @Test
    public void getBooksWithAuthor() throws Exception {
        Page<Book.WithAuthor> bookPages = bookRepo.getBooksWithAuthor(new PageRequest(0, 20));
        assertThat(bookPages.getContent(), hasSize(20));
    }
    
    @Test
    public void topRating() throws Exception {
        Page<BookWithRatings> ratings = bookRepo.topRating(new PageRequest(0, 20));
        ratings.getContent().forEach(b -> System.out.printf("%s, %s, %s, %.1f, %d%n",
                b.getBook().getTitle(),
                b.getAuthor().getName(),
                b.getPublisher().getName(),
                b.getRating(),
                b.getReadings()));
    }
    
    @Test
    public void topReadings() throws Exception {
        Page<BookWithRatings> ratings = bookRepo.topReadings(new PageRequest(0, 20));
        ratings.getContent().forEach(b -> System.out.printf("%s, %s, %s, %.1f, %d%n",
                b.getBook().getTitle(),
                b.getAuthor().getName(),
                b.getPublisher().getName(),
                b.getRating(),
                b.getReadings()));
    }
}