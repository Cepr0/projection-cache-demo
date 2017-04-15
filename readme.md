# Projection demo

_Using [Projections](http://docs.spring.io/spring-data/rest/docs/current/reference/html/#projections-excerpts.projections) 
in [Spring Data REST](http://projects.spring.io/spring-data-rest/) as [DTO](https://spring.io/blog/2016/05/03/what-s-new-in-spring-data-hopper#projections-on-repository-query-methods)._
 
Our model:

````java
 public class Book extends LongId {
 
     @NotBlank
     private final String title;
     
     @NotBlank
     @Column(unique = true)
     private final String isbn;
 
     @ManyToOne(optional = false)
     private final Author author;
 
     @ManyToOne(optional = false)
     private final Publisher publisher;
 }
 
 public class Author extends LongId {
 
     @NotBlank
     private final String name;
     
     @OneToMany(mappedBy = "author")
     private final List<Book> books = new ArrayList<>();
 }
 
 public class Publisher extends LongId {
 
     @NotBlank
     private final String name;
 
     @OneToMany(mappedBy = "publisher")
     private final List<Book> books = new ArrayList<>();
 }
 
 public class Reader extends LongId {
     
     @NotBlank
     private final String name;
     
     @NotBlank
     @Email
     @Column(unique = true)
     private final String email;
     
     @ManyToMany
     @JoinTable(name = "readings", joinColumns = @JoinColumn(name = "reader_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
     private final List<Book> books = new ArrayList<>();
 }
 
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
 }
````

The last entity **Reading** is used to store information about reading the Book by the Reader with some rating (from 1 to 5).
   
Suppose we need to get a **book list** with **average rating** and the **number of reading** of each book.
To do so we can create a [Projection](http://docs.spring.io/spring-data/rest/docs/current/reference/html/#projections-excerpts.projections) 
like this:

````java
public class Book extends LongId {

    //...
    
    @Projection(name = "bookRating", types = Book.class)
    public interface Ratings {
        
        String getTitle();
        String getIsbn();
        
        @Value("#{target.author.name}")
        String getAuthor();

        @Value("#{target.publisher.name}")
        String getPublisher();
    
        /**
         * {@link ReadingRepo#getBookRatings} is used to calculate {@link Book} ratings and cache result
         */
        @Value("#{@readingRepo.getBookRatings(target)}")
        Reading.Ratings getRatings();
    }
    
    //...
}
````

And the method **getBookRatings** in the ReadingRepo repository (for other entities we also make repositories): 

````java
@RepositoryRestResource
public interface ReadingRepo extends JpaRepository<Reading, Long> {

    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book = ?1")
    Reading.Ratings getBookRatings(Book book);
}
````

that return average rating and reading count of book:
  
```java
public class Reading extends LongId {

    //...
    
    @JsonSerialize(as = Reading.Ratings.class)
    public interface Ratings {

        @JsonProperty("rating")
        Float getRating();

        @JsonProperty("readings")
        Integer getReadings();
    }
    
    //...
}
```

Now we can get a book list with ratings:

    GET http://localhost:8080/api/books?projection=bookRating
```json
{
  "_embedded" : {
    "books" : [ {
      "author" : "Author00003",
      "ratings" : {
        "rating" : 3.6666667,
        "readings" : 3
      },
      "publisher" : "Publisher00005",
      "isbn" : "00001",
      "title" : "Book00001",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/books/1"
        },
        "book" : {
          "href" : "http://localhost:8080/api/books/1{?projection}",
          "templated" : true,
          "title" : "Book"
        },
        "publisher" : {
          "href" : "http://localhost:8080/api/books/1/publisher",
          "title" : "Publisher"
        },
        "author" : {
          "href" : "http://localhost:8080/api/books/1/author",
          "title" : "Author"
        }
      }
    }, {
      "author" : "Author00001",
      "ratings" : {
        "rating" : 1.5,
        "readings" : 2
      },
      "publisher" : "Publisher00001",
      "isbn" : "00002",
      "title" : "Book00002",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/api/books/2"
        },
        "book" : {
          "href" : "http://localhost:8080/api/books/2{?projection}",
          "templated" : true,
          "title" : "Book"
        },
        "publisher" : {
          "href" : "http://localhost:8080/api/books/2/publisher",
          "title" : "Publisher"
        },
        "author" : {
          "href" : "http://localhost:8080/api/books/2/author",
          "title" : "Author"
        }
      }
    },
    ...
  },  
  "_links" : {
    "first" : {
      "href" : "http://localhost:8080/api/books?page=0&size=20",
      "title" : "First"
    },
    "self" : {
      "href" : "http://localhost:8080/api/books"
    },
    "next" : {
      "href" : "http://localhost:8080/api/books?page=1&size=20",
      "title" : "Next"
    },
    "last" : {
      "href" : "http://localhost:8080/api/books?page=2&size=20",
      "title" : "Last"
    },
    "profile" : {
      "href" : "http://localhost:8080/api/profile/books"
    },
    "search" : {
      "href" : "http://localhost:8080/api/books/search",
      "title" : "Search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 50,
    "totalPages" : 3,
    "number" : 0
  }
}
```

All looks fine but we have a 'small' issue here - for each record in this list we have an extra query to the DB that calculate ratings:

```sql
select avg(r.rating) as rating, count(r) as readings from Reading r where r.book = ?1
```

On the large database this can significantly decrease its performance.

To reduce the impact of 1+N query problem we can try to use a **cache**. First we prepare the cache for book ratings:
  
```java
@SpringBootApplication
@EnableCaching
public class ProjectionDemo {
 
    @Bean
    public CacheManager cacheManager() {
        
        Cache bookRatings = new ConcurrentMapCache("bookRatings");
 
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(asList(bookRatings));
        
        return manager;
    }
}
```

Then we add @Cacheable annotation to the repo method:

```java
@RepositoryRestResource
public interface ReadingRepo extends JpaRepository<Reading, Long> {

    @Cacheable(value = "bookRatings", key = "#a0.id")
    @RestResource(exported = false)
    @Query("select avg(r.rating) as rating, count(r) as readings from Reading r where r.book = ?1")
    Reading.Ratings getBookRatings(Book book);
}
```

To evict not actual data from the cache we can use [repository event hadler](http://docs.spring.io/spring-data/rest/docs/current/reference/html/#_writing_an_annotated_handler) 
to catch 'create', 'save' and 'delete' events:

```java
@Slf4j
@RequiredArgsConstructor
@Component
@RepositoryEventHandler(Reading.class)
public class ReadingEventHandler {

    private final @NonNull CacheManager cacheManager;
    
    @HandleAfterCreate
    @HandleAfterSave
    @HandleAfterDelete
    public void evictCaches(Reading reading) {
        Book book = reading.getBook();
        cacheManager.getCache("bookRatings").evict(book.getId());
 
        LOG.info("<<< Ratings caches evicted >>>");
    }
}
```

Now the second and the next calls of ```GET http://localhost:8080/api/books?projection=bookRating```
 will take ratings data from the cache.