package com.example.baggins.moviesearch;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Baggins on 29.11.2015.
 */

//"backdrop_sizes": [
//        "w300",
//        "w780",
//        "w1280",
//        "original"
//        ],
//        "logo_sizes": [
//        "w45",
//        "w92",
//        "w154",
//        "w185",
//        "w300",
//        "w500",
//        "original"
//        ],
//        "poster_sizes": [
//        "w92",
//        "w154",
//        "w185",
//        "w342",
//        "w500",
//        "w780",
//        "original"
//        ],
//        "profile_sizes": [
//        "w45",
//        "w185",
//        "h632",
//        "original"
//        ],
//        "still_sizes": [
//        "w92",
//        "w185",
//        "w300",
//        "original"
//        ]

enum posterSize {
    W92 ("w92"),
    W154 ("w154"),
    W185 ("w185"),
    W342 ("w342"),
    W500 ("w500"),
    W780 ("w780"),
    ORIGINAL ("original");

    private String size;

    private posterSize(String size) {
        this.size = size;
    }

    public String toString() {
        return size;
    }
}

enum backdropSize {
    W300("w300"),
    W780("w780"),
    W1280("w1280"),
    ORIGINAL("original");

    private String size;

    private backdropSize(String size) {
        this.size = size;
    }

    public String toString() {
        return size;
    }
}

public class TMDbAPI {
    private static String tmdbPagePath = "http://api.themoviedb.org/3/discover/movie?";
    private static String tmdbMoviePath = "http://api.themoviedb.org/3/movie/";

    static String genreString = "{\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":10769,\"name\":\"Foreign\"},{\"id\":36,\"name\":\"History\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV Movie\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]}";
    private static TMDbAPI ourInstance;
    private static RequestQueue queue;
    private static Context context;
    static String apiKey = "ead87e4bbbd8f401b961e16bd311981e";
    private Integer startDate = 0;
    private Integer endDate = 0;
    private Integer page = 1;
    String language = null;

    JSONObject genres;
    Integer genrePosition = 0;

    public static TMDbAPI getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new TMDbAPI(context);
        return ourInstance;
    }

    private TMDbAPI(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        try {
            genres = new JSONObject(genreString);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    RequestQueue getRequestQueue(){
        return queue;
    }

    void setDate(Integer startDate, Integer endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
    void setStartDate(Integer startDate){this.startDate = startDate;}
    void setEndDate(Integer endDate) {this.endDate = endDate;}
    Integer getStartDate() {return startDate;}
    Integer getEndDate() {return endDate;}
    void cancelDate() {
        startDate = 0;
        endDate = 0;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public void cancelLanguage()
    {
        language = null;
    }

    public void sendPageRequest(Response.Listener<JSONObject> listener){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, makePageUrl(), listener , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsObjRequest);
    }

    String makePageUrl(){
        String url = tmdbPagePath;
        if(startDate != 0)
           url = url.concat("primary_release_date.gte=" + startDate.toString() + "-01-01&");
        if(endDate != 0)
            url = url.concat("primary_release_date.lte=" + endDate.toString() + "-12-31&");
        if(language != null)
            url = url.concat("language=" + language + "&");
        if(genrePosition != null)
            url = url.concat("with_genres=" + getGenresIds()[genrePosition].toString() + "&");
        if(page !=null)
            url = url.concat("page=" + page.toString() + "&");
        url = url.concat("api_key=" + apiKey);
        return  url;
    }

    String[] getGenresNames() {
        try {
            JSONArray genresArray  = genres.getJSONArray("genres");
            String genresString[] = new String[genresArray.length()];
            for(int i = 0; i < genresArray.length(); i++)
                genresString[i] = genresArray.getJSONObject(i).getString("name");
            return genresString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    Integer[] getGenresIds() {
        try {
            JSONArray genresArray  = genres.getJSONArray("genres");
            Integer genresString[] = new Integer[genresArray.length()];
            for(int i = 0; i < genresArray.length(); i++)
                genresString[i] = genresArray.getJSONObject(i).getInt("id");
            return genresString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    void setGenrePosition(Integer i){ genrePosition = i;}
    void cancelGenre() {genrePosition = null;}

    void setPage(Integer page) {
        if(page >0 && page <1000) //max in tmdb page = 1000
            this.page = page;
    }
    Integer getPage() { return page;}

    public void sendImagesRequest(Integer filmId, Response.Listener<JSONObject> listener) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, makeImagesUrl(filmId), listener , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsObjRequest);
    }

    String makeImagesUrl(Integer id){
        String url  = tmdbMoviePath.concat(id.toString());
        url = url.concat("/images?api_key=");
        url = url.concat(apiKey);
        return url;
    }

}
