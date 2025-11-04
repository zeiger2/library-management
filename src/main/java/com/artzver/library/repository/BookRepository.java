package com.artzver.library.repository;

import com.artzver.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByAvailableCopiesGreaterThan(Integer copies);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title% AND b.author.lastName = :authorLastName")
    List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("authorLastName") String authorLastName);

    @Query("SELECT b FROM Book b JOIN b.loans l WHERE l.dueDate < CURRENT_TIMESTAMP AND l.status = 'ACTIVE'")
    List<Book> findOverdueBooks();
}