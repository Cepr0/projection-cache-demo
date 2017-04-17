package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.rest.core.annotation.Description;
import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Publisher;

/**
 * Simple DTO to store rating and reading count values of {@link Book}, {@link Author} or {@link Publisher}
 * <p>in {@link Book.WithRatings}, {@link Author.WithRatings} and {@link Publisher.WithRatings} projections
 */
@Description("Book ratings")
@JsonSerialize(as = Ratings.class)
public interface Ratings {

    @JsonProperty("rating")
    Float getRating();

    @JsonProperty("readings")
    Integer getReadings();
}
