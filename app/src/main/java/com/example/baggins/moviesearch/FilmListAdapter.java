package com.example.baggins.moviesearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageRequest;
import com.koushikdutta.ion.Ion;

/**
 * Created by Baggins on 01.12.2015.
 */
public class FilmListAdapter extends ArrayAdapter<TMDbFilm> {

    Context context;
    TMDbFilm[] films;

    public FilmListAdapter(Context context, TMDbFilm[] films) {
        super(context, R.layout.film_list_row, films);
        this.context = context;
        this.films = films;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.film_list_row, parent, false);

        ImageView poster = (ImageView)convertView.findViewById(R.id.poster);
        Ion.with(context)
                .load(films[position].getPosterUrl(posterSize.W185))
                .withBitmap().
                placeholder(R.drawable.poster_placeholder_w185)
                .intoImageView(poster);

        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(films[position].title);

        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        rating.setText(films[position].vote_average.toString());

        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        String genreString = "";
        for(int i = 0; i < films[position].genres.length; i++)
            genreString = genreString.concat(films[position].genres[i] + " ");
        genre.setText(genreString);

        TextView date = (TextView) convertView.findViewById(R.id.releaseYear);
        date.setText(films[position].release_date);

        return convertView;
    }
}
