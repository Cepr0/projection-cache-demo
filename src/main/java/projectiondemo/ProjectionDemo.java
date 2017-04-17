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
import projectiondemo.rest.ReadingEventHandler;

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
    
    /**
     * Create three caches to store a result of calculating the ratings of {@link Book}, {@link Author} and {@link Publisher}
     * <p>in their projections {@link Book.WithRatings}, {@link Author.WithRatings} and {@link Publisher.WithRatings}.
     * <p>Caches filled in {@link ReadingRepo} 'getXXXRatings' methods
     * and evicted in {@link ReadingEventHandler#evictCaches}
     */
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
            final Integer AUTHORS_NUMBER = 10;
            final Integer PUBLISHERS_NUMBER = 5;
            final Integer BOOKS_NUMBER = 50;
            final Integer READERS_NUMBER = 100;
            final Integer READINGS_NUMBER = 200;
            
            rangeClosed(1, AUTHORS_NUMBER).forEach(i ->
                    authorRepo.save(new Author(format("Author%05d", i))));

            rangeClosed(1, PUBLISHERS_NUMBER).forEach(i ->
                    publisherRepo.save(new Publisher(format("Publisher%05d", i))));
            
            for (int i = 1; i <= BOOKS_NUMBER; i++) {
                bookRepo.save(new Book(
                        format("Book%05d", i),
                        format("%05d", i),
                        authorRepo.findOne(current().nextLong(1, AUTHORS_NUMBER + 1)),
                        publisherRepo.findOne(current().nextLong(1, PUBLISHERS_NUMBER + 1))));
            }
    
            rangeClosed(1, READERS_NUMBER).forEach(i ->
                    readerRepo.save(new Reader(format("Reader%05d", i), format("reader%05d@mail.com", i))));
    
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

