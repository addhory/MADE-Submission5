package com.example.katalogfilm.feature.searchfrag;

import com.example.katalogfilm.data.api.ApiRetrofit;
import com.example.katalogfilm.data.entity.MovieResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private SearchView view;

    public SearchPresenter(SearchView view){
        this.view=view;
    }

    public void searchMv(String mv) {
        view.showLoad();
        Call<MovieResult> listMovie = ApiRetrofit.getService().getSearchMovie(mv);

        listMovie.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                view.finishLoad();
                if (response.body() != null) {
                    view.showList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }
    void getList(){
        view.showLoad();
        Call<MovieResult> listMovie = ApiRetrofit.getService().getUpcoming();

        listMovie.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                view.finishLoad();
                if (response.body() != null) {
                    view.showList(response.body().getResult());
                }
            }
            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                view.noData();
            }
        });
    }
}

