package DAOimplement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAOInterface.BookDAO;
import database.Database;
import model.Book;
import model.User;

public class BookDAOImpl implements BookDAO {
    private static final Logger logger = Logger.getLogger(BookDAOImpl.class.getName());
    private static final String FIND_BOOK_BY_TITLE = "SELECT * FROM books WHERE title = ?";
    private static final String FIND_BOOK_BY_Id = "SELECT * FROM books WHERE id = ?";
    private static final String INSERT_BOOK = "insert into books(title, category, author_id) values(?,?,?)";
    private static final String UPDATE_USER = "update books set user_id = ?  where title = ?";
    private static final String FIND_CATEGORY = "select * from books where category = ?";


    @Override
    public boolean addBook(Book book, User user) {
        try (Connection connection = Database.getConnection()) {
            if (user == null || book == null || user.getUserRole() == null || !user.getUserRole().equals("admin")) {
                logger.info("User is null or has no role");
                return false;
            }
            connection.setAutoCommit(false);
            if (!isBookExist(book.getTitle(), connection)) {
                insertIntoBook(book, connection);
                connection.commit();
                logger.info("Book added successfully");
                return true;
            }
            logger.info("Book already exists");
            return false;
        }
        catch(SQLException e) {
            logger.log(Level.SEVERE, "error exist while getting connection");
            return false;
        }

    }

    @Override
    public void borrowBook(String bookTitle, String username) {
        UserDaoImpl userDao = new UserDaoImpl();
        Book book = findBookByTitle(bookTitle).orElse(null);
        User user = userDao.getUserByUsername(username).orElse(null);

        try (Connection connection = Database.getConnection();
             PreparedStatement insertBorrowQuery = connection.prepareStatement("UPDATE books SET user_id = ? WHERE title = ?")) {
            if (book != null && book.getUserId() != 0) {
                logger.info("book already borrowed");
                return ;
            }
            connection.setAutoCommit(false);
            if (user != null && book != null ) {
                System.out.println(user.getId() + " " + book.getTitle());
                insertBorrowQuery.setInt(1, user.getId());
                insertBorrowQuery.setString(2, book.getTitle());
                insertBorrowQuery.executeUpdate();
                connection.commit();
                logger.info("Book borrowed successfully");
            } else {
                logger.info("Book not found or user");
                connection.rollback();
            }
        } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error exists while borrowing book", e);

        }
    }

    @Override
    public void returnBook(String bookTitle) {
        try(Connection connection = Database.getConnection();
        PreparedStatement removeBorrowed = connection.prepareStatement(UPDATE_USER)) {
            if (isBookExist(bookTitle, connection)) {
                connection.setAutoCommit(false);
                removeBorrowed.setString(1, null);
                removeBorrowed.setString(2, bookTitle );
                removeBorrowed.executeUpdate();
                connection.commit();
                logger.info("the book returned successfully");
                return;
            }
            logger.info("the book" + bookTitle + "is not found");

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while return book");
        }
    }


    @Override
    public Optional<Book> findBookById(int bookId) {
        try (Connection connection = Database.getConnection();
        PreparedStatement findBookQuery = connection.prepareStatement(FIND_BOOK_BY_Id) ){
            findBookQuery.setInt(1, bookId);
            ResultSet rs = findBookQuery.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setAuthorId(rs.getInt("author_id"));
                return Optional.of(book);
            }

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while return book");
        }
        return Optional.empty();

    }

    @Override
    public Optional<Book> findBookByTitle(String bookTitle) {
        try (Connection connection = Database.getConnection();
             PreparedStatement findByName = connection.prepareStatement(FIND_BOOK_BY_TITLE)) {
            findByName.setString(1, bookTitle);
            ResultSet rs = findByName.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setUserId(rs.getInt("user_id"));
                return Optional.of(book);
            }

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while find book by title", e);
        }
        return Optional.empty();

    }

    @Override
    public Optional<List<Book>> listBooks() {
        try (Connection connection = Database.getConnection();
        PreparedStatement findAllQuery = connection.prepareStatement("select * from books")) {
            connection.setAutoCommit(false);
            ResultSet rs = findAllQuery.executeQuery();
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setUserId(rs.getInt("user_id"));
                books.add(book);
                connection.commit();
            }
            return Optional.of(books);
        }catch (SQLException e) {
            logger.log(Level.SEVERE, "error while list books");
        }
        return Optional.empty();

    }

    @Override
    public Optional<List<Book>> listBooksOfCategory(String category) {
        try (Connection connection = Database.getConnection();
        PreparedStatement findAllQuery = connection.prepareStatement(FIND_CATEGORY)) {
            connection.setAutoCommit(false);
            findAllQuery.setString(1, category);
            ResultSet rs = findAllQuery.executeQuery();
            if (rs.next()) {
                List<Book>books = new ArrayList<>();
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("category"), rs.getInt("author_id")));
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setCategory(category);
                    book.setAuthorId(rs.getInt("author_id"));
                    books.add(book);
                }
                connection.commit();
                return Optional.of(books);
            }
            logger.info("there are no category with name of " + category);


        }catch (SQLException e) {
            logger.log(Level.SEVERE, "error while list books by Category", e);
        }
        return Optional.empty();

    }

    public boolean isBookExist(String title, Connection connection) {

        try (PreparedStatement findByNameQuery = connection.prepareStatement(FIND_BOOK_BY_TITLE)) {
            findByNameQuery.setString(1,title);
            ResultSet resultSet = findByNameQuery.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error exist while checking if book exists",e);
            return false;
        }
    }

    private void insertIntoBook(Book book,  Connection connection) {

        try (PreparedStatement insertQuery = connection.prepareStatement(INSERT_BOOK)) {
            insertQuery.setString(1, book.getTitle());
            insertQuery.setString(2, book.getCategory());
            insertQuery.setInt(3, book.getAuthorId());
            insertQuery.executeUpdate();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error exist while inserting book");
        }
    }

}

