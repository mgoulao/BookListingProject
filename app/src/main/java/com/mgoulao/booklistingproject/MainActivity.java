package com.mgoulao.booklistingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String MG_TAG = "MG";
    boolean filled = false;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setElevation(0);
        } catch (NullPointerException e) {
            Log.e("MainActivity", "Problem setting the Actoin Bar elevation", e);
        }


        ImageButton searchBooks = (ImageButton) findViewById(R.id.search_books);
        searchBox = (EditText) findViewById(R.id.search_box);

        Log.d(MG_TAG, searchBox.getText().toString());
        /*
        * Listener for the Search button
        * Checks if the EditText is not empty and starts the BooksListActivity
        */
        searchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBox.getText().toString();
                isEmpty(searchQuery);
                if (filled) {
                    Intent intent = new Intent(MainActivity.this, BooksListActivity.class);
                    intent.putExtra("QUERY", searchQuery);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please fill the search box" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void isEmpty(String key) {
        if (key.equals("")) {
            filled = false;
        } else {
            filled = true;
        }

    }



}
