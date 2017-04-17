package projectiondemo.dto;

import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Publisher;
import projectiondemo.repo.BookRepo;

/**
 * Projection that defines Book ratings DTO - an attempt to use projections in repositories methods
 * to expose DTO with Spring Data REST.
 *
 * See methods {@link BookRepo#topRating} and {@link BookRepo#topReadings}
 */
// @JsonSerialize(as = BookWithRatings.class)
public interface BookWithRatings {
    Book getBook();
    Author getAuthor();
    Publisher getPublisher();
    Double getRating();
    Long getReadings();
}
