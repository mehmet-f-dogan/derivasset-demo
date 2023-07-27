package dev.mehmetfd.derivassetdemo.services.caching;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

/**
 * The type Entity caching service.
 */
@Service
@EnableCaching
public class EntityCachingService {

  private AuthorRepository authorRepository;
  private BookRepository bookRepository;

  /**
   * Instantiates a new Entity caching service.
   *
   * @param authorRepository the author repository
   * @param bookRepository   the book repository
   */
  @Autowired
  public EntityCachingService(AuthorRepository authorRepository, BookRepository bookRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
  }

  /**
   * Gets cached author.
   *
   * @param authorId the author iid
   * @return the cached author
   */
  @Cacheable(value = "authors", unless = "#result == null")
  public Author getCachedAuthor(Long authorId) {
    return authorRepository.findById(authorId).orElse(null);
  }

  /**
   * Gets cached book.
   *
   * @param bookId the book id
   * @return the cached book
   */
  @Cacheable(value = "books", unless = "#result == null")
  public Book getCachedBook(Long bookId) {
    return bookRepository.findById(bookId).orElse(null);
  }

  /**
   * Evict author from cache.
   *
   * @param authorId the author id
   */
  @CacheEvict(value = "authors", condition = "#authorId != null")
  public void evictAuthorFromCache(Long authorId) {
    Author  author = authorRepository.findById(authorId).orElse(null);
    if (author != null) {
      for (Book book : author.getBooks()) {
        evictBookFromCache(book.getId());
      }	
    }
  }

  @CacheEvict(value = "authors", condition = "#authorId != null")
  public void evictOnlyAuthorFromCache(Long authorId) {
  }

  /**
   * Evict book from cache.
   *
   * @param bookId the book id
   */
  @CacheEvict(value = "books", condition = "#bookId != null")
  public void evictBookFromCache(Long bookId) {
    Book book = bookRepository.findById(bookId).orElse(null);
    Author author = book.getAuthor();
    evictOnlyAuthorFromCache(author.getId());
  }
}
