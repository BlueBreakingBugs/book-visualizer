package com.joblesscoders.arbook.pojo;

import java.util.List;

public class BookResponse {
    private String message;
    private Integer statusCode;
    private List<Book> books;

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public List<Book> getBooks() {
        return books;
    }
}
