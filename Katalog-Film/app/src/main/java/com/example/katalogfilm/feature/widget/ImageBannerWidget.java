package com.example.katalogfilm.feature.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.katalogfilm.R;
import com.example.katalogfilm.adapter.MovieAdapter;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class ImageBannerWidget extends AppWidgetProvider {
    public static final String TOAST_ACT = "com.example.katalogfilm.TOAST_ACTION";
    public static final String ON_CLICK_FAV_ACT = "com.example.katalogfilm.ON_CLICK_FAVORITE_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.image_banner_widget);
        views.setRemoteAdapter(R.id.stack_views, intent);
        views.setEmptyView(R.id.stack_views, R.id.empty_view);

        Intent toastIntent = new Intent(context, ImageBannerWidget.class);
        toastIntent.setAction(ImageBannerWidget.TOAST_ACT);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_views, toastPendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        /*switch (Objects.requireNonNull(intent.getAction())) {
            case TOAST_ACT:

                String title  = intent.getStringExtra(MovieAdapter.EXTRA_MOVIE);

                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                break;
            case ON_CLICK_FAV_ACT: {
                int widgetIDs[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, ImageBannerWidget.class));
                onUpdate(context, appWidgetManager, widgetIDs);
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetIDs, R.id.stack_views);
                break;
            }
        }*/
        if(Objects.requireNonNull(intent.getAction()).equals(TOAST_ACT)){
            String title  = intent.getStringExtra(MovieAdapter.EXTRA_MOVIE);

            Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
        }
        ComponentName thisWidget = new ComponentName(context, ImageBannerWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_views);
        super.onReceive(context, intent);
    }
}

