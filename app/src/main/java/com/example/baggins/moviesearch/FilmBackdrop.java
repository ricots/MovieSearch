package com.example.baggins.moviesearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Baggins on 02.12.2015.
 */
public class FilmBackdrop {
    static final String prefix_path = "https://image.tmdb.org/t/p/";
    String filePath;
    Integer height, width;

    FilmBackdrop(JSONObject backdrop) {
        try {
            filePath = backdrop.getString("file_path");
            height = backdrop.getInt("height");
            width = backdrop.getInt("width");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String getUrl(backdropSize size){
        String url = prefix_path.concat(size.toString());
        url = url.concat(filePath);
        return url;
    }
}
