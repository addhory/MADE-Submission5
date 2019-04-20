package com.example.favoritemovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.favoritemovie.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.favoritemovie.db.DatabaseContract.CONTENT_URI;
import static com.example.favoritemovie.db.DatabaseContract.favoriteColumns.MOVIE_ID;
import static com.example.favoritemovie.db.DatabaseContract.favoriteColumns.OVERVIEW;
import static com.example.favoritemovie.db.DatabaseContract.favoriteColumns.POSTER_PATH;
import static com.example.favoritemovie.db.DatabaseContract.favoriteColumns.TITLE;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";

    @BindView(R.id.tv_detail_title_movie)
    TextView mvTitle;
    @BindView(R.id.tv_movie_overview_detail)
    TextView mvOverview;
    @BindView(R.id.img_detail_movie)
    CircleImageView imgDetail;
    private ActionBar toolbar;
    private Movie mv;
    private Boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_detail_movie);

        ButterKnife.bind(this);

        if(getSupportActionBar()!=null){
            toolbar=getSupportActionBar();
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        mv = getIntent().getParcelableExtra(EXTRA_MOVIE);
        showLoad();
        isFavorite=checkFav();

    }

    private void showLoad(){
            this.setTitle(mv.getTitle());
            mvTitle.setText(mv.getTitle());
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w780" + mv.getPosterPath())
                    .into(imgDetail);
            mvOverview.setText(mv.getOverview());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isFavorite) {
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            menu.findItem(R.id.btn_fav).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.btn_fav:
                invalidateOptionsMenu();
                toggleFavorite(item);
                return true;
        }
        return false;
    }
    private void toggleFavorite(MenuItem item) {
        if (isFavorite) {
            Uri uri = Uri.parse(CONTENT_URI+"/" + mv.getId());
            getContentResolver().delete(uri,null,null);
            //fvMovieHelper.delete(mv.getId());
            Toast.makeText(this,R.string.remove_fav, Toast.LENGTH_SHORT).show();
            isFavorite = false;
        } else {
            saveMovie(mv);
            //fvMovieHelper.insert(mv);
            Toast.makeText(this,R.string.add_fav, Toast.LENGTH_SHORT).show();
            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            isFavorite = true;
        }
    }
    private void saveMovie(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_ID, movie.getId());
        contentValues.put(TITLE, movie.getTitle());
        contentValues.put(OVERVIEW, movie.getOverview());
        contentValues.put(POSTER_PATH, movie.getPosterPath());
        getContentResolver().insert(CONTENT_URI, contentValues);
    }
    private boolean checkFav(){
        //SQLiteDatabase sqLiteDatabase= helper.getWritableDatabase();
        //String query = "SELECT * FROM " + TABLE_FV + " WHERE movie_id = " +mv.getId();
        Uri uri = Uri.parse(CONTENT_URI+"/"+mv.getId());
        Cursor c = getContentResolver().query(uri,null,null,null,null);
        if(c!=null){
            return c.getCount()>0;
        }
        return false;

    }


}
