package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.core.Relation;
import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Publisher;

/**
 * @author Cepro, 2017-04-17
 */
@Relation(value = "book", collectionRelation = "books")
public class BookWithRatingsImpl implements BookWithRatings {
    
    private BookWithRatings book;
    
    public BookWithRatingsImpl(BookWithRatings bookWithRatings) {
        this.book = bookWithRatings;
    }
    
    @JsonIgnore
    @Override
    public Book getBook() {
        return book.getBook();
    }
    
    @JsonIgnore
    @Override
    public Author getAuthor() {
        return book.getAuthor();
    }
    
    @JsonIgnore
    @Override
    public Publisher getPublisher() {
        return book.getPublisher();
    }
    
    @Override
    public Double getRating() {
        return book.getRating();
    }
    
    @Override
    public Long getReadings() {
        return book.getReadings();
    }
}
