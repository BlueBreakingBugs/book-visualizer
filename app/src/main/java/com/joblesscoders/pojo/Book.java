package com.joblesscoders.pojo;

import java.util.List;

public class Book {
    private String title,isbn,id,description,thumbnail,publisher;
    private List<String> author;
    private List<Contents> contents;


    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<String> getAuthor() {
        return author;
    }

    public List<Contents> getContents() {
        return contents;
    }
}
