package com.artzver.library.repository;

import com.artzver.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Author> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT DISTINCT a FROM Author a JOIN a.books b")
    List<Author> findAuthorsWithBooks();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.author.id = :authorId")
    Long countBooksByAuthorId(Long authorId);
}