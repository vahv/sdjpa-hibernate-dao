package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {

    public Book saveNewBook(Book book);

    public Book deleteBookById(Long id);

    public Book getById(Long id);

    public Book updateBook(Book book);

    public Book findBookByTitle(String title);

}
