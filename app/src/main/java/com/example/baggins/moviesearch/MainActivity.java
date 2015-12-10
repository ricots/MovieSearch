package com.example.baggins.moviesearch;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.Response;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_DATE = 822;
    private static final int RESULT_UNDEFINED = 564;
    private static final int RESULT_GENRE = 459;

    Boolean isLoading = false;
    Button dateButton, searchButton, genreButton;
    LinearLayout filmList;
    TMDbAPI tmDbAPI;
    Context context;
    ProgressBar topProgressBar, bottomProgressBar;
    ScrollView scrollView;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmDbAPI = TMDbAPI.getInstance(this);
        dateButton = (Button) findViewById(R.id.date_button);
        dateButton.setOnClickListener(this);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        genreButton = (Button) findViewById(R.id.genre_button);
        genreButton.setOnClickListener(this);
        filmList = (LinearLayout) findViewById(R.id.films_list_layout);
        topProgressBar = (ProgressBar) findViewById(R.id.top_progress_bar);
        bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress_bar);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        bottomProgressBar.setVisibility(View.GONE);
        scrollView.setOnScrollChangeListener(onFilmListScrollListener());
        context = this;
        onClickSearchButton();
        //getWindow().setStatusBarColor(Color.BLUE);
    }

    View.OnScrollChangeListener onFilmListScrollListener() {
        return new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                Integer diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                if (diff <= -300 && !isLoading) {
                    isLoading = true;
                    bottomProgressBar.setVisibility(View.VISIBLE);
                    if (tmDbAPI.getPage() < 100)
                        tmDbAPI.setPage(tmDbAPI.getPage() + 1);
                    tmDbAPI.sendPageRequest(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            addFilmsToLinearLayout(filmList, response);
                            isLoading = false;
                            bottomProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_button: {onClickDateButton(); break;}
            case R.id.search_button: {onClickSearchButton(); break;}
            case R.id.genre_button: {onClickGenreButton(); break;}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_DATE: {onActivityResultDate(resultCode, data); break;}
            case RESULT_GENRE: {onActivityResultGenre(resultCode, data); break;}
        }
    }

    private void onActivityResultGenre(int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
            genreButton.setText(tmDbAPI.getGenresNames()[tmDbAPI.genrePosition]);
    }

    void onActivityResultDate(int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                tmDbAPI.setStartDate(data.getIntExtra("startDate", tmDbAPI.getStartDate()));
                tmDbAPI.setEndDate(data.getIntExtra("endDate", tmDbAPI.getEndDate()));
                dateButton.setText("Date:" + tmDbAPI.getStartDate().toString() + "-" + tmDbAPI.getEndDate().toString());
                break;
            case RESULT_UNDEFINED:
                tmDbAPI.cancelDate();
                dateButton.setText("SET DATE");
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    void onClickDateButton() {
        Intent intent = new Intent(this, DateOptionActivity.class);
        intent.putExtra("startDate", tmDbAPI.getStartDate());
        intent.putExtra("endDate", tmDbAPI.getEndDate());
        startActivityForResult(intent, RESULT_DATE);
    }

    void onClickSearchButton() {
        topProgressBar.setVisibility(View.VISIBLE);
        tmDbAPI.setPage(1);
        filmList.removeAllViews();
        tmDbAPI.sendPageRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addFilmsToLinearLayout(filmList, response);
                topProgressBar.setVisibility(View.GONE);
            }
        });
    }

    void addFilmsToLinearLayout(LinearLayout list, JSONObject films) {
        final TMDbFilmPage tmDbParser = new TMDbFilmPage(films);
        FilmListAdapter listAdapter = new FilmListAdapter(context, tmDbParser.getFilms());
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, null);
            final int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullFilmActivity.class);
                    intent.putExtra("film", tmDbParser.getFilms()[finalI]);
                    startActivity(intent);
                }
            });
            filmList.addView(item);
        }
    }

    void onClickGenreButton() {
        Intent intent = new Intent(this, GenreOptionActivity.class);
        startActivityForResult(intent, RESULT_GENRE);
    }
}
