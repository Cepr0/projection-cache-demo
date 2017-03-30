package projectiondemo;

import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import projectiondemo.domain.*;
import projectiondemo.repo.*;

import java.sql.SQLException;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.rangeClosed;

@SpringBootApplication
@EnableCaching
public class ProjectionDemo {
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
    
    @Bean
    public CacheManager cacheManager() {
        
        Cache bookRatings = new ConcurrentMapCache("bookRatings");
        Cache authorRatings = new ConcurrentMapCache("authorRatings");
        ConcurrentMapCache publisherRatings = new ConcurrentMapCache("publisherRatings");

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(asList(bookRatings, authorRatings, publisherRatings));
        
        return manager;
    }

	public static void main(String[] args) {
		SpringApplication.run(ProjectionDemo.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(AuthorRepo authorRepo, PublisherRepo publisherRepo, BookRepo bookRepo, ReaderRepo readerRepo, ReadingRepo readingRepo) {
        return args -> {
            final Integer AUTHORS_NUMBER = 1000;
            final Integer PUBLISHERS_NUMBER = 50;
            final Integer BOOKS_NUMBER = 10000;
            final Integer READERS_NUMBER = 1000;
            final Integer READINGS_NUMBER = 10000;
            
            // Author author1 = new Author("Author1");
            // Author author2 = new Author("Author2");
            // Author author3 = new Author("Author3");
            // authorRepo.save(asList(author1, author2, author3));
    
            rangeClosed(1, AUTHORS_NUMBER).forEach(i ->
                    authorRepo.save(new Author(format("Author%05d", i))));

            // Publisher p1 = new Publisher("Publisher1");
            // Publisher p2 = new Publisher("Publisher2");
            // publisherRepo.save(asList(p1, p2));
    
            rangeClosed(1, PUBLISHERS_NUMBER).forEach(i ->
                    publisherRepo.save(new Publisher(format("Publisher%05d", i))));
            
            // Book book1 = new Book("Book1", "1234", author1, p1);
            // Book book2 = new Book("Book2", "2345", author1, p1);
            // Book book3 = new Book("Book3", "3456", author2, p1);
            // Book book4 = new Book("Book4", "4567", author2, p2);
            // Book book5 = new Book("Book5", "5678", author3, p2);
            // Book book6 = new Book("Book6", "6789", author3, p2);
            // bookRepo.save(asList(book1, book2, book3, book4, book5, book6));
    
            for (int i = 1; i <= BOOKS_NUMBER; i++) {
                bookRepo.save(new Book(
                        format("Book%05d", i),
                        format("%05d", i),
                        authorRepo.findOne(current().nextLong(1, AUTHORS_NUMBER + 1)),
                        publisherRepo.findOne(current().nextLong(1, PUBLISHERS_NUMBER + 1))));
            }
    
            // Reader reader1 = new Reader("Reader1", "reader1@mail.com");
            // Reader reader2 = new Reader("Reader2", "reader2@mail.com");
            // readerRepo.save(asList(reader1, reader2));
    
            rangeClosed(1, READERS_NUMBER).forEach(i ->
                    readerRepo.save(new Reader(format("Reader%05d", i), format("reader%05d@mail.com", i))));
    
//             Reading r1 = new Reading(reader1, book1, "review1_1", 4);
//             Reading r2 = new Reading(reader1, book2, "review1_2", 2);
//             Reading r3 = new Reading(reader1, book3, "review1_3", 3);
//             Reading r4 = new Reading(reader1, book4, "review1_4", 4);
//             Reading r5 = new Reading(reader1, book5, "review1_5", 3);
//             Reading r6 = new Reading(reader1, book6, "review1_6", 4);
//             Reading r7 = new Reading(reader2, book1, "review2_1", 2);
//             Reading r8 = new Reading(reader2, book2, "review2_2", 3);
//             Reading r9 = new Reading(reader2, book3, "review2_3", 4);
//             Reading r10 = new Reading(reader2, book4, "review2_4", 5);
//             Reading r11 = new Reading(reader2, book5, "review2_5", 4);
// //            Reading r12 = new Reading(reader2, book6, "review2_6", 3);
//             readingRepo.save(asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11/*, r12*/));
    
            for (int i = 1; i <= READINGS_NUMBER; i++) {
                try {
                    readingRepo.save(new Reading(
                            readerRepo.getOne(current().nextLong(1, READERS_NUMBER + 1)),
                            bookRepo.getOne(current().nextLong(1, BOOKS_NUMBER + 1)),
                            format("Review%05d", i),
                            current().nextInt(1, 6)));
                } catch (Exception ignore) {
                }
            }
        };
    }
}

