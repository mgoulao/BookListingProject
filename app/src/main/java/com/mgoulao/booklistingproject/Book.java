package com.mgoulao.booklistingproject;

/**
 * Created by msilv on 7/3/2017.
 */

public class Book {

    private String mThumbnail;

    private String mTitle;

    private String mAuthor;

    private Double mRating;

    public Book(String thumbnail, String title, String author, Double rating) {
        mThumbnail = thumbnail;

        mTitle = title;

        mAuthor = author;

        mRating = rating;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public Double getRating() {
        return mRating;
    }
}
