package com.mgoulao.booklistingproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by msilv on 7/3/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Activity context, ArrayList<Book> mBook) {
        super(context, 0, mBook);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list, parent, false);
        }

        Book currentBook = getItem(position);

        new DownloadImageTask((ImageView) listItemView.findViewById(R.id.thumbnail))
                .execute(currentBook.getThumbnail());

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentBook.getTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        TextView rating = (TextView) listItemView.findViewById(R.id.rating);
        rating.setText(Double.toString(currentBook.getRating()));

        GradientDrawable ratBackground = (GradientDrawable) rating.getBackground();
        ratBackground.setColor(getRatingColor(currentBook.getRating()));

        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private int getRatingColor(double rating) {
        int ratingColorResourceId;
        int magnitudeFloor = (int) Math.floor(rating);
        switch (magnitudeFloor){
            case 0:
                ratingColorResourceId = R.color.rating;
                break;
            case 1:
                ratingColorResourceId = R.color.rating_1;
                break;
            case 2:
                ratingColorResourceId = R.color.rating_2;
                break;
            case 3:
                ratingColorResourceId = R.color.rating_3;
                break;
            case 4:
                ratingColorResourceId = R.color.rating_4;
                break;
            case 5:
                ratingColorResourceId = R.color.rating_4;
                break;
            default:
                ratingColorResourceId = R.color.rating;
                break;
        }
        return ContextCompat.getColor(getContext(), ratingColorResourceId);
    }


}
