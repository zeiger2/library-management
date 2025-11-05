package com.artzver.library.config;

import com.artzver.library.entity.*;
import com.artzver.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создание категорий
        Category fiction = new Category();
        fiction.setName("Художественная литература");
        fiction.setDescription("Романы, повести, рассказы");

        Category science = new Category();
        science.setName("Научная литература");
        science.setDescription("Учебники, научные труды");

        Category history = new Category();
        history.setName("История");
        history.setDescription("Исторические книги");

        categoryRepository.save(fiction);
        categoryRepository.save(science);
        categoryRepository.save(history);

        // Создание авторов
        Author tolstoy = new Author();
        tolstoy.setFirstName("Лев");
        tolstoy.setLastName("Толстой");
        tolstoy.setBirthDate(LocalDate.of(1828, 9, 9));
        tolstoy.setBiography("Русский писатель и мыслитель");

        Author pushkin = new Author();
        pushkin.setFirstName("Александр");
        pushkin.setLastName("Пушкин");
        pushkin.setBirthDate(LocalDate.of(1799, 6, 6));
        pushkin.setBiography("Русский поэт, драматург и прозаик");

        authorRepository.save(tolstoy);
        authorRepository.save(pushkin);

        // Создание книг
        Book warAndPeace = new Book();
        warAndPeace.setTitle("Война и мир");
        warAndPeace.setIsbn("978-5-17-123456-7");
        warAndPeace.setPublicationDate(LocalDate.of(1869, 1, 1));
        warAndPeace.setAvailableCopies(5);
        warAndPeace.setAuthor(tolstoy);
        warAndPeace.setCategory(fiction);

        Book eugeneOnegin = new Book();
        eugeneOnegin.setTitle("Евгений Онегин");
        eugeneOnegin.setIsbn("978-5-17-123457-4");
        eugeneOnegin.setPublicationDate(LocalDate.of(1833, 1, 1));
        eugeneOnegin.setAvailableCopies(3);
        eugeneOnegin.setAuthor(pushkin);
        eugeneOnegin.setCategory(fiction);

        bookRepository.save(warAndPeace);
        bookRepository.save(eugeneOnegin);

        // Создание пользователей
        User admin = new User();
        admin.setEmail("admin@library.com");
        admin.setPassword("admin123");
        admin.setFirstName("Администратор");
        admin.setLastName("Системы");
        admin.setCreatedAt(LocalDateTime.now());

        User librarian = new User();
        librarian.setEmail("librarian@library.com");
        librarian.setPassword("librarian123");
        librarian.setFirstName("Библиотекарь");
        librarian.setLastName("Главный");
        librarian.setCreatedAt(LocalDateTime.now());

        userRepository.save(admin);
        userRepository.save(librarian);

        System.out.println("Тестовые данные загружены успешно!");
    }
}