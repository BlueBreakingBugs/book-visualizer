package com.joblesscoders.arbook.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Book implements Parcelable {
    private String title,isbn,_id,description,thumbnail,publisher,scale;
    private List<String> author;
    private List<Contents> contents;

    public String get_id() {
        return _id;
    }

    public String getScale() {
        return scale;
    }

    protected Book(Parcel in) {
        title = in.readString();
        isbn = in.readString();
        _id = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        publisher = in.readString();
        scale = in.readString();
        author = in.createStringArrayList();
        contents = in.createTypedArrayList(Contents.CREATOR);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(isbn);
        parcel.writeString(_id);
        parcel.writeString(description);
        parcel.writeString(thumbnail);
        parcel.writeString(publisher);
        parcel.writeString(scale);
        parcel.writeStringList(author);
        parcel.writeTypedList(contents);
    }
}
