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

  static long nextTime(int interval, int from, int to) {
    long delay = nextDelay(interval, from, to);

    return SystemClock.uptimeMillis() + delay;
  }

  static long nextDelay(int interval, int from, int to) {
    return nextAbsoluteTime(interval, from, to) - System.currentTimeMillis() + 1000;
  }

  private static long nextAbsoluteTime(int interval, int from, int to) {
    final Calendar fromTime = Calendar.getInstance();
    final Calendar toTime = Calendar.getInstance();

    final Calendar nextNotificationTime = Calendar.getInstance();
    final Calendar curTime = Calendar.getInstance();

    setCalendarHour(fromTime, from);
    setCalendarHour(toTime, to);
    setCalendarHour(nextNotificationTime, from);

    if (from < to) {
      if (curTime.before(fromTime)) {
        return nextNotificationTime.getTimeInMillis();
      }

      if (curTime.after(toTime)) {
        nextNotificationTime.add(Calendar.DAY_OF_YEAR, 1);
        return nextNotificationTime.getTimeInMillis();
      }

      moveToIntervalAfterCurrent(nextNotificationTime, curTime, interval);

      return nextNotificationTime.getTimeInMillis();

    } else {
      if (curTime.before(toTime)) {
        nextNotificationTime.add(Calendar.DAY_OF_YEAR, -1);
        moveToIntervalAfterCurrent(nextNotificationTime, curTime, interval);

        return nextNotificationTime.getTimeInMillis();
      }

      if (curTime.after(fromTime)) {
        moveToIntervalAfterCurrent(nextNotificationTime, curTime,  interval);

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
