package com.joblesscoders.arbook.utils;

import com.joblesscoders.arbook.pojo.BookRequest;
import com.joblesscoders.arbook.pojo.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface RestApiInterface {
    @POST("books")
    Call<BookResponse> getBookData(@Body BookRequest bookRequest);
}
