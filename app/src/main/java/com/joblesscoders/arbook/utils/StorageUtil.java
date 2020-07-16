package com.joblesscoders.arbook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.joblesscoders.arbook.pojo.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageUtil {
    private static final String LIST_KEY = "books";
    private static final String PREF_NAME = "book_pref";
    public static boolean addBookToList(Book book, Context context) {

        // get existing books
        List<String> books = getBookList(context);

        // check if the book is already present
        for (String b: books) {
            if (b.equals(book.get_id())) {
                // duplicate found, abort mission
                return false;
            }
        }

        // add to list
        books.add(book.get_id());
        writeToPref(books, context);
        return true;
    }

    public static boolean removeBookFromList(String id, Context context) {
        List<String> list = getBookList(context);
        boolean result = list.remove(id);
        if (!result) {
            // book wasn't present anyway
            return false;
        }
        writeToPref(list, context);
        return true;
    }

    private static void writeToPref(List<String> books, Context context) {
        StringBuilder s = new StringBuilder();
        for (String b: books) {
            s.append(b).append(" ");
        }
        s = new StringBuilder(s.toString().trim());

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, s.toString());
        editor.apply();
    }

    public static ArrayList<String> getBookList(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String id_list = pref.getString(LIST_KEY, "");

        ArrayList<String> list = new ArrayList<>();
        if (id_list.equals(""))
            return list;

        Collections.addAll(list, id_list.split(" "));
        return list;
    }
}
