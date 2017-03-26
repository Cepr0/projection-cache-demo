package projectiondemo.domain;

import projectiondemo.domain.base.LongId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;

import static org.hibernate.annotations.FetchMode.*;

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

    @Fetch(JOIN)
    @ManyToOne(optional = false)
    private final Author author;
    
    @Projection(name = "bookRating", types = Book.class)
    public interface Rating {
        
        String getTitle();
        String getIsbn();
        
        @Value("#{target.author.name}")
        String getAuthor();
        
        @Value("#{@readingRepo.getBookRating(target)}")
        Float getRating();
    }
    
    @Projection(name = "withAuthor", types = Book.class)
    public interface WithAuthor {
        
        String getTitle();
        String getIsbn();
    
        @Value("#{target.author.name}")
        String getAuthor();
    }
}
