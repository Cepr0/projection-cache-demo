package projectiondemo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import projectiondemo.domain.base.LongId;
import projectiondemo.repo.ReadingRepo;
import projectiondemo.dto.Ratings;

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
     * Projection that defines {@link Book} DTO with it ratings
     */
    @Projection(name = "bookRating", types = Book.class)
    public interface WithRatings {
        
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
        Ratings getRatings();
    }
    
    @Projection(name = "bookAuthor", types = Book.class)
    public interface WithAuthor {
        
        String getTitle();
        String getIsbn();
    
        @Value("#{target.author.name}")
        String getAuthor();
    }

    @Projection(name = "bookPublisher", types = Book.class)
    public interface WithPublisher {

        String getTitle();
        String getIsbn();

        @Value("#{target.publisher.name}")
        String getPublisher();
    }
}
