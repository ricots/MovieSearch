package com.example.baggins.moviesearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.koushikdutta.ion.Ion;

public class FilmGalleryAdapter extends ArrayAdapter<FilmBackdrop> {

    private Context context;
    private FilmBackdrop[] filmBackdrops;

    public FilmGalleryAdapter(Context context, FilmBackdrop[] objects) {
        super(context, R.layout.gallery_row, objects);
        this.context = context;
        filmBackdrops = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.gallery_row, parent, false);
        ImageView view = (ImageView) convertView.findViewById(R.id.image);
        Ion.with(context)
                .load(filmBackdrops[position].getUrl(BackdropSize.W780))
                .withBitmap()
                .placeholder(R.drawable.backdrop_placeholder_w780)
                .intoImageView(view);
        return convertView;
    }
}
