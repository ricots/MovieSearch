package com.example.baggins.moviesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Baggins on 28.11.2015.
 */
public class TMDbFilmPage {

    private JSONObject page;
    private JSONArray films;

    public TMDbFilmPage(JSONObject response) {
        this.page = response;
        try {
            films = response.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFilmTitles() {
        String titles = new String();
        for(int i = 0; i < films.length(); i++)
            titles = titles.concat(getFilmTitle(i) + "\n");
        return titles;
    }
    public String getFilmTitle(Integer i) {
        String title = new String();
        try {
            title = films.getJSONObject(i).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return title;
    }
    public String[] getFilmTitlesMassive() {
        String titles[] = new String[films.length()];
        try {
            for(int i = 0; i < films.length(); i++) {
                titles[i] = films.getJSONObject(i).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return titles;
    }

    public Integer[] getFilmIds() {
        Integer ids[] = new Integer[films.length()];
        try {
            for(int i = 0; i < films.length(); i++) {
                ids[i] = films.getJSONObject(i).getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return ids;
    }
    public Integer getFilmId(Integer i){
        Integer id = 0;
        try {
            id = films.getJSONObject(i).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return id;
    }

    public Integer getPageNumber() {
        Integer page = 0;
        try {
            page = this.page.getInt("page");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return page;
    }
    public Integer getTotalPages() {
        Integer totalPages = 0;
        try {
            totalPages = page.getInt("total_pages");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalPages;
    }
    public Integer getTotalResults() {
        Integer totalResults = 0;
        try {
            totalResults = page.getInt("total_results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalResults;
    }
    public Integer getFilmsNumber() {return films.length();}

    public TMDbFilm[] getFilms() {
        TMDbFilm film[] = new TMDbFilm[films.length()];
        for (int i = 0; i < films.length(); i++)
            try {
                film[i] = new TMDbFilm(films.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return  film;
    }
}
