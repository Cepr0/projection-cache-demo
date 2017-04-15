package projectiondemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import projectiondemo.domain.base.LongId;
import projectiondemo.repo.ReadingRepo;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cepro, 2017-03-24
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author extends LongId {

    @NotBlank
    private final String name;
    
    @OneToMany(mappedBy = "author")
    private final List<Book> books = new ArrayList<>();
    
    /**
     * Projection that defines {@link Author} DTO with it ratings
     */
    @Projection(name = "authorRating", types = Author.class)
    public interface Ratings {
        
        String getName();
    
        /**
         * {@link ReadingRepo#getAuthorRatings} is used to calculate {@link Author} ratings and cache result
         */
        @JsonProperty("ratings")
        @Value("#{@readingRepo.getAuthorRatings(target)}")
        Reading.Ratings getRatings();
    }
}
