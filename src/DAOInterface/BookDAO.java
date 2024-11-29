package DAOInterface;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    boolean addBook(Book book, User user);
    void borrowBook(String bookTitle, String username);
    void returnBook(String bookTitle);
    Optional<Book> findBookById(int bookId);
    Optional<Book> findBookByTitle(String bookTitle);
    Optional<List<Book>> listBooks();
    Optional<List<Book>> listBooksOfCategory(String category);
}