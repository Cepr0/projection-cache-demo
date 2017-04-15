package projectiondemo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import projectiondemo.domain.base.LongId;
import projectiondemo.repo.BookRepo;
import projectiondemo.repo.ReadingRepo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Cepro, 2017-03-24
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends LongId {

    @NotBlank
    private final String title;
    
    @NotBlank
    @Column(unique = true)
    private final String isbn;

    @ManyToOne(optional = false)
    private final Author author;

    @ManyToOne(optional = false)
    private final Publisher publisher;
    
    /**
     * Projection that defines Book ratings DTO - an attempt to use projections in repositories methods
     * to expose DTO with Spring Data REST.
     *
     * See methods {@link BookRepo#topRating} and {@link BookRepo#topReadings}
     */
    public interface BookRatings {
        Book getBook();
        Author getAuthor();
        Publisher getPublisher();
        Double getRating();
        Long getReadings();
    }
    
    /**
     * Projection that defines {@link Book} DTO with it ratings
     */
    @Projection(name = "bookRating", types = Book.class)
    public interface Ratings {
        
        String getTitle();
        String getIsbn();
        
        @Value("#{target.author.name}")
        String getAuthor();

        @Value("#{target.publisher.name}")
        String getPublisher();
    
        /**
         * {@link ReadingRepo#getBookRatings} is used to calculate {@link Book} ratings and cache result
         */
        @Value("#{@readingRepo.getBookRatings(target)}")
        Reading.Ratings getRatings();
    }
    
    @Projection(name = "bookAuthor", types = Book.class)
    public interface BookAuthor {
        
        String getTitle();
        String getIsbn();
    
        @Value("#{target.author.name}")
        String getAuthor();
    }

    @Projection(name = "bookPublisher", types = Book.class)
    public interface BookPublisher {

        String getTitle();
        String getIsbn();

        @Value("#{target.publisher.name}")
        String getPublisher();
    }
}
