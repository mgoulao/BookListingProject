package com.mgoulao.booklistingproject;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by msilv on 7/4/2017.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private static final String MG_TAG = "MG";
    String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        Log.d(MG_TAG, "BookLoader()");
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        Log.d(MG_TAG, "loadInBackground()");
        if (mUrl == null) {
            return null;
        }

        ArrayList<Book> bookList = QueryUtils.fetchBookData(mUrl);
        return bookList;
    }
}
