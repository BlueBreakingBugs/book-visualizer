package com.joblesscoders.arbook.pojo;

import java.util.ArrayList;
import java.util.List;

public class BookRequest {
    private List<String> id;

    public BookRequest(List<String> id) {
        this.id = id;
    }

    public BookRequest(String bid) {
        id = new ArrayList<>();
        id.add(bid);
    }
}
