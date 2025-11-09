package com.artzver.library.controller;

import com.artzver.library.dto.CreateBookDTO;
import com.artzver.library.entity.Author;
import com.artzver.library.entity.Book;
import com.artzver.library.entity.Category;
import com.artzver.library.repository.AuthorRepository;
import com.artzver.library.repository.BookRepository;
import com.artzver.library.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Author author;
    private Category category;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();

        author = new Author("Лев", "Толстой");
        author = authorRepository.save(author);

        category = new Category();
        category.setName("Художественная литература");
        category.setDescription("Романы");
        category = categoryRepository.save(category);
    }

    @Test
    @WithMockUser
    void getAllBooks_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void createBook_ShouldReturnCreatedBook() throws Exception {
        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Война и мир");
        createBookDTO.setIsbn("978-5-17-123456-7");
        createBookDTO.setPublicationDate(LocalDate.of(1869, 1, 1));
        createBookDTO.setAvailableCopies(5);
        createBookDTO.setAuthorId(author.getId());
        createBookDTO.setCategoryId(category.getId());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Война и мир"))
                .andExpect(jsonPath("$.isbn").value("978-5-17-123456-7"));
    }

    @Test
    @WithMockUser
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        Book book = new Book("Война и мир", "978-5-17-123456-7",
                LocalDate.of(1869, 1, 1), 5);
        book.setAuthor(author);
        book.setCategory(category);
        book = bookRepository.save(book);

        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("Война и мир"));
    }

    @Test
    @WithMockUser
    void getBookById_WhenBookNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void searchBooks_ShouldReturnMatchingBooks() throws Exception {
        Book book = new Book("Война и мир", "978-5-17-123456-7",
                LocalDate.of(1869, 1, 1), 5);
        book.setAuthor(author);
        book.setCategory(category);
        bookRepository.save(book);

        mockMvc.perform(get("/api/books/search")
                        .param("title", "война"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Война и мир"));
    }
}