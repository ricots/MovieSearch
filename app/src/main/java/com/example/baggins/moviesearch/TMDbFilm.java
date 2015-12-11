package com.example.baggins.moviesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TMDbFilm implements Serializable {

    private static String path_poster_prefix= "https://image.tmdb.org/t/p/";
    Integer id = null;
    Boolean adult = null;
    String backdrop_path = null;
    Integer genre_ids[] = null;
    String genres[] = null;
    String original_language = null;
    String original_title = null;
    String overview = null;
    String release_date = null;
    String poster_path = null;
    Double popularity = null;
    String title = null;
    Double vote_average = null;
    Integer vote_count = null;

    public TMDbFilm(JSONObject film)  {
        try {
            id = film.getInt("id");
            adult = film.getBoolean("adult");
            backdrop_path = film.getString("backdrop_path");
            genre_ids = getGenreIDs(film);
            genres = getGenres();
            original_language = film.getString("original_language");
            original_title = film.getString("original_title");
            overview = film.getString("overview");
            release_date = film.getString("release_date");
            poster_path = film.getString("poster_path");
            popularity = film.getDouble("popularity");
            title = film.getString("title");
            vote_average = film.getDouble("vote_average");
            vote_count = film.getInt("vote_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public Integer[] getGenreIDs(JSONObject film) {
        try {
            JSONArray genreArray = film.getJSONArray("genre_ids");
            Integer genre_ids[] = new Integer[genreArray.length()];
            for (int i = 0; i < genreArray.length(); i++) {
                genre_ids[i] = genreArray.getInt(i);
            }
            return  genre_ids;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public String[] getGenres() {
            try {
                String genresString[] = new String[genre_ids.length];
                JSONObject TMDbAPIGenresObject = new JSONObject(GlobalStrings.genreString);
                JSONArray TMDbAPIGenresObjectArray = TMDbAPIGenresObject.getJSONArray("genres");
                for(int i = 0; i < genre_ids.length; i++) {
                    for(int j = 0; j < TMDbAPIGenresObjectArray.length(); j++) {
                        JSONObject jsonObject = TMDbAPIGenresObjectArray.getJSONObject(j);
                        if(jsonObject.getInt("id") == genre_ids[i]) {
                            genresString[i] = jsonObject.getString("name");
                            break;
                        }
                    }
                }
                return genresString;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

    }
    public String getPosterUrl(PosterSize size) {
        String url;
        url = path_poster_prefix.concat(size.toString());
        url = url.concat(poster_path);
        return url;
    }
    public String getBackdropUrl(BackdropSize size) {
        String url;
        url = path_poster_prefix.concat(size.toString());
        url = url.concat(backdrop_path);
        return url;
    }
}
