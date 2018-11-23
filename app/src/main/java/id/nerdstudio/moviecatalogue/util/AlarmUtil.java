package id.nerdstudio.moviecatalogue.util;

import android.content.Context;

import java.util.Calendar;

import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AlarmReceiver;

public class AlarmUtil {

    public static void setReminder(Context context) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        if (!alarmReceiver.isAlarmSet(context, AlarmReceiver.TYPE_DAILY_REMINDER)) {
            Calendar dailyReminder = Calendar.getInstance();
            dailyReminder.set(Calendar.HOUR_OF_DAY, 7);
            dailyReminder.set(Calendar.MINUTE, 0);
            dailyReminder.set(Calendar.SECOND, 0);
            alarmReceiver.setRepeatingAlarm(context, AlarmReceiver.TYPE_DAILY_REMINDER,
                    dailyReminder, context.getString(R.string.movie_catalogue_daily_reminder));
        }
        if (alarmReceiver.isAlarmSet(context, AlarmReceiver.TYPE_RELEASE_TODAY)) {
            Calendar releaseTodayReminder = Calendar.getInstance();
            releaseTodayReminder.set(Calendar.HOUR_OF_DAY, 8);
            releaseTodayReminder.set(Calendar.MINUTE, 0);
            releaseTodayReminder.set(Calendar.SECOND, 0);
            alarmReceiver.setRepeatingAlarm(context, AlarmReceiver.TYPE_RELEASE_TODAY,
                    releaseTodayReminder, "");
        }
    }

    public static void cancelReminder(Context context) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_DAILY_REMINDER);
        alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_RELEASE_TODAY);
    }
}
