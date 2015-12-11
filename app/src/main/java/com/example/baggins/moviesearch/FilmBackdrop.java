package com.example.baggins.moviesearch;

import org.json.JSONException;
import org.json.JSONObject;

public class FilmBackdrop {
    private static final String prefix_path = "https://image.tmdb.org/t/p/";
    private String filePath;
    private Integer height, width;

    FilmBackdrop(JSONObject backdrop) {
        try {
            filePath = backdrop.getString("file_path");
            height = backdrop.getInt("height");
            width = backdrop.getInt("width");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    String getUrl(BackdropSize size){
        String url = prefix_path.concat(size.toString());
        url = url.concat(filePath);
        return url;
    }
    Integer GetHeight() {return height;}
    Integer GetWidth() {return width;}
}
