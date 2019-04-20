package com.example.katalogfilm.feature.settingactivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.katalogfilm.R;
import com.example.katalogfilm.feature.scheduler.DailySchedulerReceiver;
import com.example.katalogfilm.feature.scheduler.ReleseTodaySchedulerReceiver;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingFrag())
                .commit();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.release))){
            releaseServices(sharedPreferences.getBoolean(key,false));
        }else if(key.equals(getString(R.string.daily))){
            dailyReminder(sharedPreferences.getBoolean(key,false));
        }
    }
    private void releaseServices(boolean handle) {

        if (handle) {

            enableReleaseServices();
        } else {

            disableReleaseServices();
        }
    }
    private void dailyReminder(boolean handle) {

        if (handle) {
            enableDailyReminder();
        } else {

            disableDailyReminder();
        }
    }
    private void enableReleaseServices() {

        setAlarm(ReleseTodaySchedulerReceiver.class, ReleseTodaySchedulerReceiver.NOTIF_ID_RELEASE, 8, 0);
        Toast.makeText(this, R.string.enable_notification, Toast.LENGTH_SHORT).show();
    }
    private void disableReleaseServices() {

        stopAlarm(ReleseTodaySchedulerReceiver.class, ReleseTodaySchedulerReceiver.NOTIF_ID_RELEASE);
        Toast.makeText(this, R.string.cancel_notification, Toast.LENGTH_SHORT).show();
    }
    private void enableDailyReminder() {
        setAlarm(DailySchedulerReceiver.class, DailySchedulerReceiver.NOTIF_ID_DAILY, 7, 0);
        Toast.makeText(this, R.string.enable_remider, Toast.LENGTH_SHORT).show();
    }
    private void disableDailyReminder() {

        stopAlarm(DailySchedulerReceiver.class, DailySchedulerReceiver.NOTIF_ID_DAILY);
        Toast.makeText(this, R.string.disable_remider, Toast.LENGTH_SHORT).show();
    }
    private void stopAlarm(Class cls, int notifId) {

        Intent intent = new Intent(this, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifId, intent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {

            alarmManager.cancel(pendingIntent);
        }
    }
    private void setAlarm(Class cls, int notifId, int hours, int minutes) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, cls);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifId, intent, 0);
        if (alarmManager != null) {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public static class SettingFrag extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.ref_reminder);
        }
    }
}
