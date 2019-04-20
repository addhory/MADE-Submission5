package com.example.favoritemovie;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.favoritemovie.adapter.FvAdapter;
import com.example.favoritemovie.entity.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favoritemovie.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_listFav)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshFav)
    SwipeRefreshLayout swipeRefreshLayout;
    private FvAdapter adapter;
    @BindView(R.id.tvFav)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new FvAdapter(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadFavAsync().execute();
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        new LoadFavAsync().execute();
    }

    private class LoadFavAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            textView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI,null,null,null,null);

        }
        @Override
        protected void onPostExecute(Cursor mv){
            super.onPostExecute(mv);
            swipeRefreshLayout.setRefreshing(false);
            textView.setVisibility(View.INVISIBLE);
            Cursor list = mv;
            adapter.setListMv(list);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            int count=0;
            try{
                count=((list.getCount()>0)?list.getCount():0);
            }catch (Exception e){
                Log.w("ERROR", e.getMessage());
            }
            if (count==0){
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
}
