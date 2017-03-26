package projectiondemo;

import projectiondemo.domain.Author;
import projectiondemo.domain.Book;
import projectiondemo.domain.Reader;
import projectiondemo.domain.Reading;
import projectiondemo.repo.AuthorRepo;
import projectiondemo.repo.BookRepo;
import projectiondemo.repo.ReaderRepo;
import projectiondemo.repo.ReadingRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.asList;

@SpringBootApplication
@EnableCaching
public class ProjectionDemo {
    
    @Bean
    public CacheManager cacheManager() {
        
        Cache bookRating = new ConcurrentMapCache("bookRating");
        Cache authorRating = new ConcurrentMapCache("authorRating");
        Cache bookAuthor = new ConcurrentMapCache("bookAuthor");
        
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(asList(bookRating, authorRating, bookAuthor));
        
        return manager;
    }

	public static void main(String[] args) {
		SpringApplication.run(ProjectionDemo.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(AuthorRepo authorRepo, BookRepo bookRepo, ReaderRepo readerRepo, ReadingRepo readingRepo) {
        return args -> {
            Author author1 = new Author("Author1");
            Author author2 = new Author("Author2");
            Author author3 = new Author("Author3");
            authorRepo.save(asList(author1, author2, author3));
    
            Book book1 = new Book("Book1", "1234", author1);
            Book book2 = new Book("Book2", "2345", author1);
            Book book3 = new Book("Book3", "3456", author2);
            Book book4 = new Book("Book4", "4567", author2);
            Book book5 = new Book("Book5", "5678", author3);
            Book book6 = new Book("Book6", "6789", author3);
            bookRepo.save(asList(book1, book2, book3, book4, book5, book6));
    
            Reader reader1 = new Reader("Reader1", "reader1@mail.com");
            Reader reader2 = new Reader("Reader2", "reader2@mail.com");
            readerRepo.save(asList(reader1, reader2));
    
            Reading r1 = new Reading(reader1, book1, "review1_1", 1);
            Reading r2 = new Reading(reader1, book2, "review1_2", 2);
            Reading r3 = new Reading(reader1, book3, "review1_3", 3);
            Reading r4 = new Reading(reader1, book4, "review1_4", 4);
            Reading r5 = new Reading(reader1, book5, "review1_5", 5);
            Reading r6 = new Reading(reader1, book6, "review1_6", 4);
            Reading r7 = new Reading(reader2, book1, "review2_1", 2);
            Reading r8 = new Reading(reader2, book2, "review2_2", 3);
            Reading r9 = new Reading(reader2, book3, "review2_3", 4);
            Reading r10 = new Reading(reader2, book4, "review2_4", 5);
            Reading r11 = new Reading(reader2, book5, "review2_5", 4);
            Reading r12 = new Reading(reader2, book6, "review2_6", 3);
            readingRepo.save(asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12));
        };
    }
}

