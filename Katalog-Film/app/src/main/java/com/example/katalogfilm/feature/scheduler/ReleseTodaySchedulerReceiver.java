package com.example.katalogfilm.feature.scheduler;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.katalogfilm.R;
import com.example.katalogfilm.data.api.ApiRetrofit;
import com.example.katalogfilm.data.entity.Movie;
import com.example.katalogfilm.data.entity.MovieResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleseTodaySchedulerReceiver extends BroadcastReceiver {
    public static final int NOTIF_ID_RELEASE = 2;
    public static CharSequence CHANNEL_NAME = "ChannelMovie";
    public static String CHANNEL_ID = "Movie";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Call<MovieResult> listMovie = ApiRetrofit.getService().getNowPlaying();
        @SuppressLint("SimpleDateFormat") final String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        listMovie.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (response.body() != null) {
                    for(Movie movie:response.body().getResult()){
                        if(movie.getReleaseDate().equals(today)){
                            showNotification(context, movie);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
            }
        });
    }
    private void showNotification(Context context, Movie movie){
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_black_24dp))
                .setContentTitle(movie.getTitle())
                .setContentText(movie.getOverview())
                .setSubText(movie.getReleaseDate())
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null) {

            notificationManager.notify(NOTIF_ID_RELEASE, notification);
        }
    }
}
