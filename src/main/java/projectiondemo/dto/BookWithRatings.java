package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Publisher;
import projectiondemo.repo.BookRepo;

/**
 * Projection that defines Book ratings Dto - an attempt to use projections in repositories methods
 * to expose Dto with Spring Data REST.
 *
 * See methods {@link BookRepo#topRating} and {@link BookRepo#topReadings}
 */
// @JsonSerialize(as = BookWithRatings.class)
// TODO Implement @Dto
// Prototype: @Dto(baseEntity=Book.class, rel="topRating", value = "book", collectionRelation = "books")
public interface BookWithRatings extends Dto<Book, Long> {
    Book getBook();
    Author getAuthor();
    Publisher getPublisher();
    Double getRating();
    Long getReadings();
    
    // TODO Find a better way to implement this
    @Override
    default Class<Book> getBaseEntity() {
        return Book.class;
    }

    @JsonIgnore
    @Override
    default Long getId() {
        return getBook().getId();
    }

    default String getTitle() {
        return getBook().getTitle();
    }

    default String getIsbn() {
        return getBook().getIsbn();
    }

    @JsonProperty("author")
    default String getAuthorName() {
        return getAuthor().getName();
    }

    @JsonProperty("publisher")
    default String getPublisherName() {
        return getPublisher().getName();
    }


}
