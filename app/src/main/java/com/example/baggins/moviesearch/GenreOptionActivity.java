package com.example.baggins.moviesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GenreOptionActivity extends Activity implements View.OnClickListener {

    ListView genreListView ;
    TMDbAPI tmDbAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_option);
        genreListView = (ListView) findViewById(R.id.genre_list_view);
        tmDbAPI = TMDbAPI.getInstance(this);
        setAdapter();
        genreListView.setOnItemClickListener(OnItemClickListener());
    }

    void setAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tmDbAPI.getGenresNames());
        genreListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    AdapterView.OnItemClickListener OnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                tmDbAPI.setGenrePosition(position);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }
}
