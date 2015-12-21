package com.example.baggins.moviesearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FullFilmActivity extends AppCompatActivity {

    private TMDbFilm tmDbFilm;
    private ImageView backdropImage, titleImage;
    private TextView title, realiseYear, overview, popularity, voteCount;
    private ProgressBar progressBar;
    private LinearLayout gallery;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_film);



        Intent intent = getIntent();
        tmDbFilm = (TMDbFilm) intent.getSerializableExtra("film");

        backdropImage = (ImageView) findViewById(R.id.backdrop_image);
        Ion.with(this)
                .load(tmDbFilm.getBackdropUrl(BackdropSize.W1280))
                .withBitmap()
                .placeholder(R.drawable.backdrop_placeholder_w300)
                .intoImageView(backdropImage);
        backdropImage.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
        backdropImage.getLayoutParams().height = getWindowManager().getDefaultDisplay().getWidth()*720/1280;

        titleImage = (ImageView) findViewById(R.id.title_image);
        Ion.with(this)
                .load(tmDbFilm.getPosterUrl(PosterSize.W342))
                .withBitmap()
                .placeholder(R.drawable.poster_placeholder_w342)
                .intoImageView(titleImage);

        title = (TextView) findViewById(R.id.title);
        title.setText(tmDbFilm.title);

        realiseYear = (TextView) findViewById(R.id.realise_year);
        realiseYear.setText(tmDbFilm.release_date);

        overview = (TextView) findViewById(R.id.overview);
        overview.setText(tmDbFilm.overview);

        popularity = (TextView) findViewById(R.id.popularity);
        popularity.setText(tmDbFilm.vote_average.toString());

        voteCount = (TextView) findViewById(R.id.vote_count);
        voteCount.setText(tmDbFilm.vote_count.toString());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(tmDbFilm.vote_average.intValue() * 10);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        gallery = (LinearLayout) findViewById(R.id.gallery);
        addFilmsToGallery();

        if(getResources().getConfiguration().orientation == 2) {
            RelativeLayout relativeLayout = (RelativeLayout)  findViewById(R.id.head);
            relativeLayout.setVisibility(View.GONE);
            overview.setVisibility(View.GONE);
        }

        context = this;
    }
    public void addFilmsToGallery() {
        TMDbAPI tmDbAPI = TMDbAPI.getInstance(this);
        tmDbAPI.sendImagesRequest(tmDbFilm.id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                FilmBackdrop[] filmBackdrops = getFilmBackdrops(response);
                FilmGalleryAdapter galleryAdapter = new FilmGalleryAdapter(context, filmBackdrops);
                for (int i = 1; i < galleryAdapter.getCount(); i++) {
                    View item = galleryAdapter.getView(i, null, null);
                    gallery.addView(item);
                }
            }
        });
    }
    public FilmBackdrop[] getFilmBackdrops(JSONObject response) {
        FilmBackdrop filmBackdrops[] = null;
        try {
            JSONArray jsonArray = response.getJSONArray("backdrops");
            filmBackdrops = new FilmBackdrop[jsonArray.length()];
            for(int i = 1; i < jsonArray.length(); i++){
                filmBackdrops[i] = new FilmBackdrop(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filmBackdrops;
    }

}
