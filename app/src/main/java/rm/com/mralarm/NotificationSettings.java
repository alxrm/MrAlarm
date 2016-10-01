package rm.com.mralarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static rm.com.mralarm.TimeUtils.FROM_TIMES;
import static rm.com.mralarm.TimeUtils.INTERVALS;
import static rm.com.mralarm.TimeUtils.TO_TIMES;

/**
 * Created by alex
 */

final class NotificationSettings {

  private final static String KEY_INTERVAL = "intervalIndex";
  private final static String KEY_FROM = "fromIndex";
  private final static String KEY_TO = "toIndex";
  private final static String KEY_ON = "isOn";

  private SharedPreferences prefSrc;
  private SharedPreferences.Editor prefEditor;

  @SuppressLint("CommitPrefEdits")
  NotificationSettings(Context context) {
    prefSrc = PreferenceManager.getDefaultSharedPreferences(context);
    prefEditor = prefSrc.edit();
  }

  boolean isOn() {
    return prefSrc.getBoolean(KEY_ON, false);
  }

  void setOn(boolean on) {
    prefEditor.putBoolean(KEY_ON, on);
    prefEditor.apply();
  }

  int interval() {
    return INTERVALS.valueAt(intervalIndex());
  }

  int intervalIndex() {
    return prefSrc.getInt(KEY_INTERVAL, 0);
  }

  void setInterval(int currentInterval) {
    prefEditor.putInt(KEY_INTERVAL, currentInterval);
    prefEditor.apply();
  }

  int from() {
    return FROM_TIMES.valueAt(fromIndex());
  }

  int fromIndex() {
    return prefSrc.getInt(KEY_FROM, 0);
  }

  void setFrom(int currentFrom) {
    prefEditor.putInt(KEY_FROM, currentFrom);
    prefEditor.apply();
  }

  int to() {
    return TO_TIMES.valueAt(toIndex());
  }

  int toIndex() {
    return prefSrc.getInt(KEY_TO, 0);
  }

  void setTo(int currentTo) {
    prefEditor.putInt(KEY_TO, currentTo);
    prefEditor.apply();
  }
}
