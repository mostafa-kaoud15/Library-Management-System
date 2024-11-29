package model;

public class Book {
    private int id;
    private String title;
    private int authorId;
    private String category;
    private int userId;

    public Book() {}

    public Book(int id, String title, String category, int author) {
        this.id = id;
        this.title = title;
        this.authorId = author;
        this.category = category;
    }

    public Book(String title, String category, int author) {
        this.title = title;
        this.authorId = author;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int author) {
        this.authorId = author;
    }

    public boolean isBorrowed() {
        return false;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}