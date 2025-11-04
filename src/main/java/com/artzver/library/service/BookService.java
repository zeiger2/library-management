package com.artzver.library.service;

import com.artzver.library.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    Book save(Book book);
    void deleteById(Long id);
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorId(Long authorId);
}