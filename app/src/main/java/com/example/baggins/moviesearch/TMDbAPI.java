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

public class TMDbAPI {
    private static String tmdbPagePath = "http://api.themoviedb.org/3/discover/movie?";
    private static String tmdbMoviePath = "http://api.themoviedb.org/3/movie/";
    private JSONObject genres;
    private static TMDbAPI ourInstance;
    private static RequestQueue queue;
    private static Context context;
    private Integer startDate = 0, endDate = 0, page = 1, genreListId = 0;

    public static TMDbAPI getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new TMDbAPI(context);
        return ourInstance;
    }
    private TMDbAPI(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        try {
            genres = new JSONObject(GlobalStrings.genreString);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public RequestQueue GetRequestQueue(){
        return queue;
    }
    public void setDate(Integer startDate, Integer endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public void setStartDate(Integer startDate){this.startDate = startDate;}
    public void setEndDate(Integer endDate) {this.endDate = endDate;}
    public Integer getStartDate() {return startDate;}
    public Integer getEndDate() {return endDate;}
    public void cancelDate() {
        startDate = 0;
        endDate = 0;
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
    private String makePageUrl(){
        String url = tmdbPagePath;
        if(startDate != 0)
           url = url.concat("primary_release_date.gte=" + startDate.toString() + "-01-01&");
        if(endDate != 0)
            url = url.concat("primary_release_date.lte=" + endDate.toString() + "-12-31&");
//        if(language != null)
//            url = url.concat("language=" + language + "&");
        if(genreListId != null)
            url = url.concat("with_genres=" + getGenresIds()[genreListId].toString() + "&");
        if(page !=null)
            url = url.concat("page=" + page.toString() + "&");
        url = url.concat("api_key=" + GlobalStrings.apiKey);
        return  url;
    }
    public String[] getGenresNames() {
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
    public Integer[] getGenresIds() {
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
    public void setGenreListId(Integer i){ genreListId = i;}
    public void cancelGenre() { genreListId = null;}
    public void setPage(Integer page) {
        if(page >0 && page <1000) //max in tmdb page = 1000
            this.page = page;
    }
    public Integer getPage() { return page;}
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
    private String makeImagesUrl(Integer id){
        String url  = tmdbMoviePath.concat(id.toString());
        url = url.concat("/images?api_key=");
        url = url.concat(GlobalStrings.apiKey);
        return url;
    }
    public Integer GetGenreListId() {return genreListId;}
}
