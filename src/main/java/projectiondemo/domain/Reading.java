package projectiondemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.rest.core.annotation.Description;
import projectiondemo.domain.base.LongId;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Cepro, 2017-03-24
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "readings")
public class Reading extends LongId {

    @NaturalId
    @ManyToOne(optional = false)
    private final Reader reader;

    @NaturalId
    @ManyToOne(optional = false)
    private final Book book;
    
    @NotBlank
    private String review;
    
    @Min(1) @Max(5)
    private Integer rating;
    
    /**
     * Simple DTO to store rating and reading count values of {@link Book}, {@link Author} or {@link Publisher}
     * <p>in {@link Book.Ratings}, {@link Author.Ratings} and {@link Publisher.Ratings} projections
     */
    @Description("Book ratings")
    @JsonSerialize(as = Reading.Ratings.class)
    public interface Ratings {

        @JsonProperty("rating")
        Float getRating();

        @JsonProperty("readings")
        Integer getReadings();
    }
}
