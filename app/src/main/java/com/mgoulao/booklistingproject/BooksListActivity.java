package com.mgoulao.booklistingproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BooksListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final String MG_TAG = "MG";
    public String BOOK_URL_REQUEST = "https://www.googleapis.com/books/v1/volumes?q=";
    String searchQuery = "";
    ListView listView;
    ProgressBar progressBar;
    TextView offlineTextView , cantFind;
    BookAdapter bookAdapter;
    ArrayList<Book> bookList = new ArrayList<>();
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        // get the search key from the MainActivity
        searchQuery = getIntent().getStringExtra("QUERY");
        searchQuery = searchQuery.replace(" ", "+");

        BOOK_URL_REQUEST = BOOK_URL_REQUEST + searchQuery + "&maxResults=10";
        Log.d(MG_TAG, BOOK_URL_REQUEST);

        listView = (ListView) findViewById(R.id.books_list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        offlineTextView = (TextView) findViewById(R.id.offline);
        cantFind = (TextView) findViewById(R.id.cant_find);

        bookAdapter = new BookAdapter(this, bookList);
        listView.setAdapter(bookAdapter);

        // Check the Connectivity
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            offlineTextView.setText(getResources().getString(R.string.offline));
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Log.d(MG_TAG, "onCreateLoader()");
        return new BookLoader(this, BOOK_URL_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        progressBar.setVisibility(View.INVISIBLE);

        bookAdapter.clear();

        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        } else {
            cantFind.setText(getResources().getString(R.string.no_results));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        bookAdapter.clear();
    }
}
