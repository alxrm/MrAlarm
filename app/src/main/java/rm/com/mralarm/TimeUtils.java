package rm.com.mralarm;

import android.os.SystemClock;
import android.support.v4.util.ArrayMap;

import java.util.Calendar;

/**
 * Created by alex
 */

final class TimeUtils {
  final static ArrayMap<String, Integer> FROM_TIMES = new ArrayMap<>();
  final static ArrayMap<String, Integer> TO_TIMES = new ArrayMap<>();
  final static ArrayMap<String, Integer> INTERVALS = new ArrayMap<>();

  static {
    FROM_TIMES.put("7:00", 7);
    FROM_TIMES.put("12:00", 12);
    FROM_TIMES.put("23:00", 23);

    TO_TIMES.put("23:00", 23);
    TO_TIMES.put("4:00", 4);
    TO_TIMES.put("7:00", 7);

    INTERVALS.put("5 min", 300000);
    INTERVALS.put("2 hrs", 7200000);
  }

  static long nextTimeRelative(int intervalMillis, int fromHrs, int toHrs) {
    long delay = nextDelay(intervalMillis, fromHrs, toHrs);

    return SystemClock.uptimeMillis() + delay;
  }

  static long nextDelay(int intervalMillis, int fromHrs, int toHrs) {
    return nextAbsoluteTime(intervalMillis, fromHrs, toHrs) - System.currentTimeMillis() + 1000;
  }

  private static long nextAbsoluteTime(int intervalMillis, int fromHrs, int toHrs) {
    final Calendar fromTime = Calendar.getInstance();
    final Calendar toTime = Calendar.getInstance();

    final Calendar nextNotificationTime = Calendar.getInstance();
    final Calendar curTime = Calendar.getInstance();

    setCalendarHour(fromTime, fromHrs);
    setCalendarHour(toTime, toHrs);
    setCalendarHour(nextNotificationTime, fromHrs);

    if (fromHrs < toHrs) {
      if (curTime.before(fromTime)) {
        return nextNotificationTime.getTimeInMillis();
      }

      if (curTime.after(toTime)) {
        nextNotificationTime.add(Calendar.DAY_OF_YEAR, 1);
        return nextNotificationTime.getTimeInMillis();
      }

      moveToIntervalAfterCurrent(nextNotificationTime, curTime, intervalMillis);

      return nextNotificationTime.getTimeInMillis();

    } else {
      if (curTime.before(toTime)) {
        nextNotificationTime.add(Calendar.DAY_OF_YEAR, -1);
        moveToIntervalAfterCurrent(nextNotificationTime, curTime, intervalMillis);

        return nextNotificationTime.getTimeInMillis();
      }

      if (curTime.after(fromTime)) {
        moveToIntervalAfterCurrent(nextNotificationTime, curTime,  intervalMillis);

        return nextNotificationTime.getTimeInMillis();
      }

      return nextNotificationTime.getTimeInMillis();
    }
  }

  private static void moveToIntervalAfterCurrent(Calendar nextTime, Calendar curTime, int interval) {
    while (curTime.after(nextTime)) nextTime.add(Calendar.MILLISECOND, interval);
  }

  private static void setCalendarHour(Calendar target, int hour) {
    target.set(Calendar.HOUR_OF_DAY, hour);
    target.set(Calendar.MINUTE, 0);
    target.set(Calendar.SECOND, 0);
  }
}
