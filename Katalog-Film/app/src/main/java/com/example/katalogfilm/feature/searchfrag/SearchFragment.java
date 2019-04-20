package com.example.katalogfilm.feature.searchfrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.katalogfilm.R;
import com.example.katalogfilm.adapter.MovieAdapter;
import com.example.katalogfilm.base.BaseFragment;
import com.example.katalogfilm.data.entity.Movie;

import java.util.ArrayList;

public class SearchFragment extends BaseFragment implements SearchView {
    private TextInputEditText textInputEditText;
    private SearchPresenter presenter;
    private MovieAdapter adapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_frag, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnSearch = view.findViewById(R.id.btn_search);
        textInputEditText = view.findViewById(R.id.inputSearch);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        adapter = new MovieAdapter(movieList, getActivity());
        presenter= new SearchPresenter(this);

        RecyclerView recyclerView = view.findViewById(R.id.rvMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getList();
            }
        });

        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditText.getText()!=null){
                    switch (v.getId()){
                        case R.id.btn_search:
                            presenter.searchMv(textInputEditText.getText()+"");
                            break;
                    }
                }
            }
        });
        if(savedInstanceState==null){
            presenter.getList();

        }else{
            movieList=savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            adapter.refill(movieList);
        }
    }
    @Override
    public void showLoad() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void finishLoad() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showList(ArrayList<Movie> data) {
        movieList=data;
        adapter.refill(movieList);
    }

    @Override
    public void noData() {
        swipeRefreshLayout.setRefreshing(false);
        movieList.clear();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, movieList);
        super.onSaveInstanceState(outState);
    }
}
