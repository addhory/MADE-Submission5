package com.example.katalogfilm.feature.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.katalogfilm.R;
import com.example.katalogfilm.adapter.MovieAdapter;
import com.example.katalogfilm.data.entity.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.katalogfilm.data.db.FvDatabaseContract.CONTENT_URI;

public class StackRemoteViews implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Movie> mWidgetItems = new ArrayList<>();
    private Context mContext;

    StackRemoteViews(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        final long identityToken = Binder.clearCallingIdentity();
        Cursor c = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{
                Movie mv= new Movie(c);
                mWidgetItems.add(mv);
                c.moveToNext();
            }
            while(!c.isAfterLast());
        }
        if(c!=null){
            c.close();
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie mvContent;
        Bundle extras=new Bundle();
        Bitmap bitmap = null;
        String releaseDate=null;
        String baseUrl="https://image.tmdb.org/t/p/w500";
        try{
            mvContent=mWidgetItems.get(position);
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(baseUrl+mvContent.getPosterPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            releaseDate=mvContent.getReleaseDate();
            extras.putString(MovieAdapter.EXTRA_MOVIE, mvContent.getTitle());

        }catch (InterruptedException | ExecutionException | IndexOutOfBoundsException e) {

        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        remoteViews.setTextViewText(R.id.textView, releaseDate);

        Intent intent=new Intent();
        intent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.imageView, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
