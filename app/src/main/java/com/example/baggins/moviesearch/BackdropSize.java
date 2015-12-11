package com.example.baggins.moviesearch;

public enum BackdropSize {
    W300("w300"),
    W780("w780"),
    W1280("w1280"),
    ORIGINAL("original");

    private String size;
    private BackdropSize(String size) {
        this.size = size;
    }
    public String toString() {
        return size;
    }
}
