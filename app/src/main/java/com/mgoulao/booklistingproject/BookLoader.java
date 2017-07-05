package com.mgoulao.booklistingproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Manuel Goulao on 7/4/2017.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private static final String MG_TAG = "MG";
    String mUrl;

    public BookLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchBookData(mUrl);
    }
}
