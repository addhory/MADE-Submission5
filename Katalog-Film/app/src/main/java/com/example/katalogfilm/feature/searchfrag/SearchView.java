package com.example.katalogfilm.feature.searchfrag;

import com.example.katalogfilm.data.entity.Movie;

import java.util.ArrayList;

public interface SearchView {
    void showLoad();

    void finishLoad();

    void showList(ArrayList<Movie> data);

    void noData();
}
