package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    public String getTitle() {
        return book.getBook().getTitle();
    }
    
    public String getIsbn() {
        return book.getBook().getIsbn();
    }
    
    @JsonProperty("author")
    public String getAuthorName() {
        return book.getAuthor().getName();
    }
    
    @JsonProperty("publisher")
    public String getPublisherName() {
        return book.getPublisher().getName();
    }
    
    @JsonIgnore
    @Override
    public Long getId() {
        return getBook().getId();
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
