package id.nerdstudio.moviecatalogue.config;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Calendar;

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.model.Movie;
import id.nerdstudio.moviecatalogue.util.JsonUtil;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY_REMINDER = "DailyReminder";
    public static final String TYPE_RELEASE_TODAY = "ReleaseTodayReminder";
    public static final String EXTRA_ID = "message";
    public static final String EXTRA_MESSAGE = "id";
    private final int NOTIF_ID_DAILY_REMINDER = 100;
    private final int NOTIF_ID_RELEASE_TODAY = 101;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        showAlarmNotification(context, context.getString(R.string.app_name), message, intent.getIntExtra(EXTRA_ID, 0));
    }

    private void showAlarmNotification(final Context context, final String title, final String message, final int notificationId) {
        if (notificationId == NOTIF_ID_DAILY_REMINDER) {
            showNotification(context, title, message, notificationId);
        } else {
            String currentLanguage = AppSharedPreferences.getLanguage(context);
            AppConfig.Language language = currentLanguage == null ? AppConfig.Language.en :
                    currentLanguage.equals("en") ? AppConfig.Language.en : AppConfig.Language.id;
            Ion.with(context)
                    .load(AppConfig.getCurrentMovies(AppConfig.CurrentMovieType.now_playing, language))
                    .asJsonObject()
                    .withResponse()
                    .setCallback((e, response) -> {
                        if (e != null) {
                            Log.d("Movie Release Error", e.getMessage());
                        } else {
                            JsonObject result = response.getResult();
                            if (JsonUtil.isNotNull(result, "results")) {
                                JsonArray movies = result.get("results").getAsJsonArray();
                                boolean noMovie = true;
                                for (int i = 0; i < movies.size(); i++) {
                                    JsonObject movieObject = movies.get(i).getAsJsonObject();
                                    Movie movie = Movie.fromJson(movieObject);
                                    if ((new DateTime(movie.getReleaseDate()).toLocalDate()).equals(new LocalDate())) {
                                        showNotification(context, movie.getTitle(), context.getString(R.string.movie_release_today, movie.getTitle()), notificationId + i);
                                        noMovie = false;
                                    }
                                }
                                if (noMovie) {
                                    showNotification(context, context.getString(R.string.movie_release_info), context.getString(R.string.no_movie_release_today), notificationId);
                                }
                            }
                        }
                    });
        }
    }

    private void showNotification(Context context, String title, String message, int notificationId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Channel",
                    "AlarmManagerChannel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId("Channel");

            notificationManagerCompat.createNotificationChannel(channel);
        }
        Notification notification = builder.build();
        notificationManagerCompat.notify(notificationId, notification);
    }

    public void setRepeatingAlarm(Context context, String type, Calendar calendar, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? NOTIF_ID_DAILY_REMINDER : NOTIF_ID_RELEASE_TODAY;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_ID, requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
//        long testTime = System.currentTimeMillis() + 1000;
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, testTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? NOTIF_ID_DAILY_REMINDER : NOTIF_ID_RELEASE_TODAY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? NOTIF_ID_DAILY_REMINDER : NOTIF_ID_RELEASE_TODAY;
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
}