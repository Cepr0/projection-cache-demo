package projectiondemo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import projectiondemo.domain.base.LongId;

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

    @Projection(name = "bookRating", types = Book.class)
    public interface Rating {
        
        String getTitle();
        String getIsbn();
        
        @Value("#{target.author.name}")
        String getAuthor();

        @Value("#{target.publisher.name}")
        String getPublisher();

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
