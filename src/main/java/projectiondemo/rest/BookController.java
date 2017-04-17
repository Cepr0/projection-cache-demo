package projectiondemo.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projectiondemo.dto.BookWithRatings;
import projectiondemo.repo.BookRepo;

/**
 * @author Cepro, 2017-04-16
 */
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/books")
public class BookController {
    
    private final @NonNull BookRepo bookRepo;
    private final @NonNull PagedResourcesAssembler<BookWithRatings> assembler;
    
    // TODO Implement auto implementation DTO Controllers like this
    @GetMapping("/topRating")
    public ResponseEntity<?> getRating(Pageable pageable) {
    
        return ResponseEntity.ok(assembler.toResource(bookRepo.topRating(pageable)));
    }
}
